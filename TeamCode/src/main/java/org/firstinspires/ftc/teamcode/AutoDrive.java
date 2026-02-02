package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AutoDrive {
    public boolean autoAlignEnabled = false;
    public boolean isReadyToShoot = false;
    private double turnCmd = 0;
    private double lastTurnCmd = 0;

    // Alignment Constants
    private static final double ALIGN_TOLERANCE_DEG = 2;
    private static final double SHOOTER_TOLERANCE_DEG = 2.6;
    private static final double ALIGN_TURN_MAX_VELOCITY_DEG = 60;
    private static final double ALIGN_TURN_MIN_VELOCITY_DEG = 10;
    private static final double ALIGN_TURN_MAX_POWER = 0.3;
    private static final double ALIGN_TURN_MIN_POWER = 0.07;
    private static final double K_CAM = 0.05;
    private static final double CAM_OFFSET = 0;

    // Camera Only Alignment Controller Constants
    private double kP = 0.03;
    private double kI = 0;
    private double kD = 0.0008;
    private double integralSum = 0;
    private double maxIntegralSum = 0;
    private double derivative = 0;
    private double lastError = 0;
    ElapsedTime alignmentControllerTimer = new ElapsedTime();

    public double autoAlign(double tagBearing, boolean  tagValid, double turnJoystick) {
        if (Math.abs(turnJoystick) >= 0.05) {
            turnCmd = turnJoystick;
            lastTurnCmd = turnCmd;
            resetAlignmentController();
            return turnJoystick;
        }

        double error = -(tagBearing + CAM_OFFSET);
        double dt = alignmentControllerTimer.seconds();
        dt = Range.clip(dt, 0.001, 0.05); // 1ms–50ms

        if (!tagValid) {
            turnCmd = ALIGN_TURN_MAX_POWER;
            integralSum = 0;
            derivative = 0;
        } else {

            if (Math.abs(error) <= SHOOTER_TOLERANCE_DEG) {
                isReadyToShoot = true;
            }


            if (Math.abs(error) <= ALIGN_TOLERANCE_DEG) {
               turnCmd = 0;
            } else {
                double rawDerivative = (error - lastError) / dt;
                derivative = derivative * 0.8 + rawDerivative * 0.2;

                turnCmd = kP * error + kD * derivative;

                if (turnCmd <= 0.6) {
                    integralSum += error * dt;
                }
                integralSum = Range.clip(integralSum, -maxIntegralSum, maxIntegralSum);

                turnCmd += kI *integralSum;

                if (Math.abs(turnCmd) > 0 && Math.abs(turnCmd) < ALIGN_TURN_MIN_POWER) {
                    turnCmd = Math.copySign(ALIGN_TURN_MIN_POWER, turnCmd);
                }

                turnCmd = Range.clip(turnCmd, -ALIGN_TURN_MAX_POWER, ALIGN_TURN_MAX_POWER);
                turnCmd = smooth(turnCmd, lastTurnCmd, 0.15);
            }
        }

        lastError = error;
        alignmentControllerTimer.reset();
        lastTurnCmd = turnCmd;

        return turnCmd;
    }

    public void resetAlignmentController() {
        alignmentControllerTimer.reset();
        integralSum = 0;
        derivative = 0;
        lastError = 0;
    }

    public void resetAlignment() {
        resetAlignmentController();
        isReadyToShoot = false;
    }




    // ----Not used----

    // IMU and Vision Controller Variables

    public double targetHeading = 0;
    public double targetCamera = 0;
    private double camError = 0;

    // Omega Controller Variables
    ElapsedTime angularVelocityControllerTimer = new ElapsedTime();
    private double commandedAngularVelocity = 0;
    private double angularVelocityError = 0;
    private double feedFowardOmega = 0;
    private double kVOmega = 0;
    private double kSOmega = 0;
    private double maxAccel = 0;
    private double maxDecel = 0;
    private double targetAngularVelocity = 0;
    private double kPOmega;

    // Alignment States
    public enum AlignmentStates {
        SEARCHING,
        ACQUIRING,
        TRACKING
    }
    public AlignmentStates alignState = AlignmentStates.ACQUIRING;
    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

    public double angleError(double target, double current) {
        return AngleUnit.normalizeDegrees(target - current);
    }

    public double getSmoothAlignmentPower(double angularVelocity, boolean tagValid, double tagBearing, double confidence, double robotHeading) {
        switch (alignState) {
            case SEARCHING:
                if (tagValid) {
                    alignState = AlignmentStates.ACQUIRING;
                } else {
                    turnCmd = angularVelocityController(ALIGN_TURN_MAX_VELOCITY_DEG, angularVelocity);
                }
                break;
            case ACQUIRING:
                targetHeading = robotHeading;

                targetCamera = robotHeading + tagBearing + CAM_OFFSET;
                camError = AngleUnit.normalizeDegrees(targetCamera - targetHeading);
                targetHeading += confidence * K_CAM * camError;

                double error = angleError(targetHeading, robotHeading);
                targetAngularVelocity = kPOmega * error;
                targetAngularVelocity = Range.clip(targetAngularVelocity, -ALIGN_TURN_MAX_VELOCITY_DEG, ALIGN_TURN_MAX_VELOCITY_DEG);

                turnCmd = angularVelocityController(ALIGN_TURN_MAX_VELOCITY_DEG, angularVelocity);

                alignState = AlignmentStates.TRACKING;

                break;
            case TRACKING:
                targetCamera = robotHeading + tagBearing + CAM_OFFSET;
                camError = AngleUnit.normalizeDegrees(targetCamera - targetHeading);
                targetHeading += confidence * K_CAM * camError;

                error = angleError(targetHeading, robotHeading);
                targetAngularVelocity = kPOmega * error;
                targetAngularVelocity = Range.clip(targetAngularVelocity, -ALIGN_TURN_MAX_VELOCITY_DEG, ALIGN_TURN_MAX_VELOCITY_DEG);

                if (Math.abs(error) <= ALIGN_TOLERANCE_DEG && Math.abs(angularVelocity) <= ALIGN_TURN_MIN_VELOCITY_DEG) {
                    turnCmd = 0;
                } else{
                    turnCmd = angularVelocityController(ALIGN_TURN_MAX_VELOCITY_DEG, angularVelocity);
                }

                break;
        }

        return -turnCmd;
    }

    public double angularVelocityController(double targetAngularVelocity, double currentAngularVelocity) {
        // Loop time
        double dt = angularVelocityControllerTimer.seconds();
        dt = Range.clip(dt, 0.001, 0.05); // 1ms–50ms

        // Acceleration rate clamp
        double delta = targetAngularVelocity - commandedAngularVelocity;
        double maxDelta = (delta > 0 ? maxAccel : maxDecel) * dt;
        delta = Range.clip(delta, -maxDelta, maxDelta);
        commandedAngularVelocity += delta;

        angularVelocityControllerTimer.reset();

        // Error Calculation
        angularVelocityError = commandedAngularVelocity- currentAngularVelocity;

        // Feedfoward
        feedFowardOmega = kVOmega * commandedAngularVelocity;
        feedFowardOmega += kSOmega * Math.signum(commandedAngularVelocity);

        double output = (kP * angularVelocityError) + (feedFowardOmega);

        output = Range.clip(output, -1, 1);
        return output;
    }

    public void resetAngularVelocityController() {
        commandedAngularVelocity = 0;
        angularVelocityControllerTimer.reset();
    }

    public double getAlignmentTurnPower(double tagBearing, double robotHeading, double confidence, double angularVelocity, double turnJoystick) {

        if (Math.abs(turnJoystick) >= 0.05) {
            turnCmd = turnJoystick;
            return turnJoystick;
        }
        targetCamera = AngleUnit.normalizeDegrees(robotHeading + tagBearing + CAM_OFFSET);

        camError = AngleUnit.normalizeDegrees(targetCamera - targetHeading);

        targetHeading += confidence * K_CAM * camError;

        double error = angleError(targetHeading, robotHeading);

        if (Math.abs(error) <= ALIGN_TOLERANCE_DEG && Math.abs(angularVelocity) <= ALIGN_TURN_MIN_VELOCITY_DEG) {
            turnCmd = 0;
        } else {
            turnCmd = kP * error + kD * angularVelocity;
            if (Math.abs(turnCmd) < ALIGN_TURN_MIN_POWER) {
                turnCmd = Math.copySign(ALIGN_TURN_MIN_POWER, turnCmd);
            }
            turnCmd = Range.clip(turnCmd, -ALIGN_TURN_MAX_POWER, ALIGN_TURN_MAX_POWER);
        }

        return -turnCmd;
    }


}
