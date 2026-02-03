package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class RedAutoDrive {
    public boolean autoAlignEnabled = false;
    public boolean isReadyToShoot = false;
    private ElapsedTime readyToShootTimer = new ElapsedTime();
    private boolean readyToShootTimerStarted = false;
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
                if (!readyToShootTimerStarted) {
                    readyToShootTimerStarted = true;
                    readyToShootTimer.reset();
                } else if (readyToShootTimer.seconds() >= 0.5) {
                    isReadyToShoot = true;
                    readyToShootTimerStarted = false;
                }
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
        readyToShootTimerStarted = false;
    }

    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

}
