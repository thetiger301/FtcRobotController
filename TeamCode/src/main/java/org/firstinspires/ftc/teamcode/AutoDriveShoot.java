package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AutoDriveShoot {
    public boolean autoAlignEnabled = false;
    private double targetHeadingDeg = -90;
    private double turnCmd = 0.0;
    private double lastTurnCmd = 0.0;
    private static final double ALIGN_TOLERANCE_DEG = 1;
    private static final double ALIGN_TURN_MAX_POWER = 0.6;
    private static final double MIN_TURN = 0.08;
    public double kP = 0.03;
    public double kI = 0;
    public double kD = 0.0003;

    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

    public double angleError(double target, double current) {
        return AngleUnit.normalizeDegrees(target - current);
    }

    public double getAlignmentTurnPower(double currentHeadingDeg, double angularVelocity) {
        double error = angleError(targetHeadingDeg, currentHeadingDeg);
        //double error = bearing;

        if (Math.abs(error) >= ALIGN_TOLERANCE_DEG) {
            turnCmd = kP * error + kD * angularVelocity;
            if (Math.abs(turnCmd) < MIN_TURN) {
                turnCmd = Math.copySign(MIN_TURN, turnCmd);
            }
            turnCmd = Range.clip(turnCmd, -ALIGN_TURN_MAX_POWER, ALIGN_TURN_MAX_POWER);
        } else {
            turnCmd = 0;
        }

        return -turnCmd;
    }

    public void kIUp(double change) {
        kI += change;
    }

    public void kIDown(double change) {
        kI -= change;
    }

    public void kDUp(double change) {
        kD += change;
    }

    public void kDDown(double change) {
        kD -= change;
    }

}
