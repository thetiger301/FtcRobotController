package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AutoDriveShoot {

    private PIDController aprilTagAlignPID = new PIDController(0.05, 0, 0);
    private PIDController headingHoldPID = new PIDController(0, 0, 0);
    public boolean autoAlignEnabled = false;
    private double lockedHeadingRad = 0.0;
    private boolean headingLocked = false;
    private double turnCmd = 0.0;
    private double lastTurnCmd = 0.0;
    private static final double ALIGN_TOLERANCE_DEG = 1;

    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

    public double getTurnPower(
            double joystickTurn,
            boolean tagVisible,
            double tagBearingDeg,
            double robotHeadingDeg
    ) {
        // DRIVER OVERRIDE (absolute priority)
        if (Math.abs(joystickTurn) > 0.05) {
            headingLocked = false;          // release lock
            lastTurnCmd = joystickTurn;     // immediate response
            return joystickTurn;
        }

        // AUTO-ALIGN WHEN TAG IS VISIBLE
        if (tagVisible) {

            // If NOT aligned yet → vision PID controls turn
            if (Math.abs(tagBearingDeg) > ALIGN_TOLERANCE_DEG) {
                headingLocked = false;
                turnCmd = aprilTagAlignPID.updateDrive(tagBearingDeg);

            } else {
                // TAG ALIGNED → LOCK IMU HEADING
                if (!headingLocked) {
                    lockedHeadingRad = robotHeadingDeg;
                    headingLocked = true;
                }

                double headingError = AngleUnit.normalizeRadians(lockedHeadingRad - robotHeadingDeg);
                turnCmd = headingHoldPID.updateDrive(headingError);
            }

        } else {
            // NO TAG → HOLD LAST HEADING USING IMU
            if (!headingLocked) {
                lockedHeadingRad = robotHeadingDeg;
                headingLocked = true;
            }

            double headingError = AngleUnit.normalizeRadians(lockedHeadingRad - robotHeadingDeg);
            turnCmd = headingHoldPID.updateDrive(headingError);
        }

        // SMOOTH TRANSITION (prevents snapping)
        turnCmd = smooth(turnCmd, lastTurnCmd, 0.15);

        lastTurnCmd = turnCmd;
        return Range.clip(turnCmd, 0, 1);
    }

}
