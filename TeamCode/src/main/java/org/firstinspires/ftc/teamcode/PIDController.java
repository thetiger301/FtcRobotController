package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

public class PIDController {

    private double kP, kI, kD;

    private double integralSum = 0.0;
    private double lastError = 0.0;

    private double integralLimit = 0.0;
    private boolean useIntegralLimit = false;

    public PIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setIntegralLimit(double limit) {
        integralLimit = Math.abs(limit);
        useIntegralLimit = true;
    }

    public void reset() {
        integralSum = 0.0;
        lastError = 0.0;
    }

    public double update(double error, double dt) {
        if (dt <= 0) return 0.0;

        // Integral
        integralSum += error * dt;

        if (useIntegralLimit) {
            integralSum = Range.clip(integralSum, -integralLimit, integralLimit);
        }

        // Derivative
        double derivative = (error - lastError) / dt;
        lastError = error;

        return (kP * error) + (kI * integralSum) + (kD * derivative);
    }
}
