package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class AutoDriveShoot {

    private PIDController aprilTagAlignPID = new PIDController(0, 0, 0);
    private PIDController headingHoldPID = new PIDController(0, 0, 0);
    private double lockedHeadingRad = 0.0;
    private boolean headingLocked = false;
    private double lastTurnCmd = 0.0;

    private static final double ALIGN_TOLERANCE_RAD = Math.toRadians(1.0);

    private double smooth(double target, double current, double alpha) {
        return current + alpha * (target - current);
    }

    public double getTurnPower(
            double joystickTurn,
            boolean tagVisible,
            double tagYawRad,
            double robotHeadingRad,
            double dt
    ) {
        // 1️⃣ DRIVER OVERRIDE (absolute priority)
        if (Math.abs(joystickTurn) > 0.05) {
            headingLocked = false;          // release lock
            lastTurnCmd = joystickTurn;     // immediate response
            return joystickTurn;
        }

        double turnCmd = 0.0;

        // 2️⃣ AUTO-ALIGN WHEN TAG IS VISIBLE
        if (tagVisible) {

            // If NOT aligned yet → vision PID controls turn
            if (Math.abs(tagYawRad) > ALIGN_TOLERANCE_RAD) {
                headingLocked = false;
                turnCmd = aprilTagAlignPID.update(tagYawRad, dt);

            } else {
                // 3️⃣ TAG ALIGNED → LOCK IMU HEADING
                if (!headingLocked) {
                    lockedHeadingRad = robotHeadingRad;
                    headingLocked = true;
                }

                double headingError = AngleUnit.normalizeRadians(lockedHeadingRad - robotHeadingRad);
                turnCmd = headingHoldPID.update(headingError, dt);
            }

        } else {
            // 4️⃣ NO TAG → HOLD LAST HEADING USING IMU
            if (!headingLocked) {
                lockedHeadingRad = robotHeadingRad;
                headingLocked = true;
            }

            double headingError = AngleUnit.normalizeRadians(lockedHeadingRad - robotHeadingRad);
            turnCmd = headingHoldPID.update(headingError, dt);
        }

        // 5️⃣ SMOOTH TRANSITION (prevents snapping)
        turnCmd = smooth(turnCmd, lastTurnCmd, 0.15);

        lastTurnCmd = turnCmd;
        return Range.clip(turnCmd, 1, 1);
    }

}
