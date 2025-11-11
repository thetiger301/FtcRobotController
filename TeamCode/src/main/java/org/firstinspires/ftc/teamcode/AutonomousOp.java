package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


    @Autonomous(name = "Basic Auto", group = "Autonomous")
    public class AutonomousOp extends LinearOpMode {

        public Drivetrain drivetrain;

        @Override
        public void runOpMode() {
            drivetrain = new Drivetrain(hardwareMap, telemetry, gamepad1);

            if (opModeIsActive()) {
                telemetry.addLine("Autonomous running...");
                telemetry.update();
                sleep(2000);
                telemetry.addLine("Autonomous done!");
                telemetry.update();
            }

            telemetry.addLine("Initialized");
            telemetry.update();

            // Wait for the start button
            waitForStart();

        }

    }
