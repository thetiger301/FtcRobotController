package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class PIDController {
    private double kP, kI, kD;

    // Integral Variables
    private double integralSum = 0;
    private double integralLimit = 0;
    private boolean useIntegral = false;

    // Derivative Variables
    private double derivative = 0;
    private double lastError = 0;

    // Shooter PID variables
    private double lastVelocity = 0;
    public double velocityError = 0;
    private double commandedVelocity = 0;
    private double feedFoward = 0;
    public double kP = 0.013;
    public double kV = 0.0005;
    public double kS = 0.1;
    private double maxAccel = 3000;
    private double maxDecel = 1500;

    // Timer Variables
    private ElapsedTime PIDTimer = new ElapsedTime();
    private double dt = 0;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setIntegralLimit(double limit) {
        integralLimit = limit;
        useIntegral = true;
    }

    public void reset() {
        integralSum = 0.0;
        lastError = 0.0;
    }

    public double updateDrive(double error) {
        // loop timer
        dt = PIDTimer.seconds();
        if (dt <= 0) {
            dt = 1e-3;
        }

        // Integral
        if (useIntegral) {
            integralSum += error * dt;
            integralSum = Range.clip(integralSum, -integralLimit, integralLimit);
        }

        // Derivative
        derivative = (error - lastError) / dt;
        lastError = error;

        PIDTimer.reset();

        return (kP * error) + (kI * integralSum) + (kD * derivative);
    }


    public double updateShooter(double targetVelocity, double currentVelocity) {
        // Loop time
        dt = PIDTimer.seconds();
        if (dt <= 0) {
            dt = 1e-3;
        }

        // Acceleration rate clamp
        double delta = targetVelocity - commandedVelocity;
        double maxDelta = (delta > 0 ? maxAccel : maxDecel) * dt;
        delta = Range.clip(delta, -maxDelta, maxDelta);
        commandedVelocity += delta;

        // Error Calculation
        velocityError = commandedVelocity - currentVelocity;

        // Derivative
        derivative = (currentVelocity - lastVelocity) / dt;

        //Integral
        integralSum = integralSum + (velocityError * dt);
        integralSum = Range.clip(integralSum, -5000, 5000);

        // Feedfoward
        feedFoward = kV * commandedVelocity;
        if (Math.abs(commandedVelocity) > 50) {
            feedFoward += kS * Math.signum(commandedVelocity);
        }

        double output = (kP * velocityError) + (kD * derivative) + (kI * integralSum) + (feedFoward);

        lastVelocity = currentVelocity;
        PIDTimer.reset();

        double finalOutput = Range.clip(output, -1, 1);
        return finalOutput;
    }

}
