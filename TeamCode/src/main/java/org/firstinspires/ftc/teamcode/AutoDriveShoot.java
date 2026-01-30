package org.firstinspires.ftc.teamcode;

public class AutoDriveShoot {
    public boolean autoAlignEnabled = false;
    private double turnCmd = 0.0;
    private double lastTurnCmd = 0.0;
    private static final double ALIGN_TOLERANCE_DEG = 1;

    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

}
