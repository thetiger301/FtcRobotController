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
    public double kV = 0.0006;
    public double kS = 0.18;
    private double maxAccel = 3000;
    private double maxDecel = 1500;

    // Timer Variables
    private ElapsedTime drivePIDTimer = new ElapsedTime();
    private ElapsedTime shooterPIDTimer = new ElapsedTime();
    private double dt = 0;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void reset() {
        integralSum = 0.0;
        derivative = 0.0;
        lastError = 0.0;
        lastVelocity = 0.0;
        commandedVelocity = 0.0;
    }

    public double updateDrive(double error) {
        // loop timer
        dt = drivePIDTimer.seconds();
        dt = Range.clip(dt, 0.001, 0.05); // 1ms–50ms

        // Derivative
        double rawDerivative = (error - lastError) / dt;
        derivative = derivative * 0.8 + rawDerivative * 0.2;
        lastError = error;

        double output = (kP * error) + (kD * derivative);
        if (Math.abs(output) < 1.0) {
            integralSum += error * dt;
            integralSum = Range.clip(integralSum, -2000, 2000);
        }

        drivePIDTimer.reset();

        double finalOutput = output + (kI * integralSum);
        return finalOutput;
    }


    public double updateShooter(double targetVelocity, double currentVelocity) {
        // Loop time
        dt = shooterPIDTimer.seconds();
        dt = Range.clip(dt, 0.001, 0.05); // 1ms–50ms

        // Acceleration rate clamp
        double delta = targetVelocity - commandedVelocity;
        double maxDelta = (delta > 0 ? maxAccel : maxDecel) * dt;
        delta = Range.clip(delta, -maxDelta, maxDelta);
        commandedVelocity += delta;

        // Error Calculation
        velocityError = commandedVelocity - currentVelocity;

        // Derivative
        derivative = (currentVelocity - lastVelocity) / dt;
        lastVelocity = currentVelocity;
        shooterPIDTimer.reset();

        // Feedfoward
        feedFoward = kV * commandedVelocity;
        feedFoward += kS * Math.signum(commandedVelocity);
        /*
        if (Math.abs(commandedVelocity) > 50) {
            feedFoward += kS * Math.signum(commandedVelocity);
        }
        */

        double output = (kP * velocityError) + (kD * derivative) + (feedFoward);

        //Integral
        if (Math.abs(output) < 1.0) {
            integralSum += velocityError * dt;
            integralSum = Range.clip(integralSum, -2000, 2000);
        }

        double finalOutput = output + (kI * integralSum);
        finalOutput = Range.clip(finalOutput, -1, 1);
        return finalOutput;
    }

}
