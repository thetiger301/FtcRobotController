package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Device Declarations
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Competition Program
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
