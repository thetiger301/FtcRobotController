package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


    @Autonomous(name = "Basic Auto", group = "Autonomous")
    public class AutonomousOp extends LinearOpMode {

        public Drivetrain drivetrain;


        public double axial, lateral, yaw;

        @Override
        public void runOpMode() {
            drivetrain = new Drivetrain(hardwareMap, telemetry, gamepad1);


            telemetry.addLine("Initialized");
            telemetry.update();

            // Wait for the start button
            waitForStart();

            if (opModeIsActive()) {

                if (opModeIsActive()) {
                    // Move forward 12 inches
                    drivetrain.driveForwardDistance(12, 0.5);
                    sleep(500);




                    telemetry.addLine("Autonomous running...");
                    telemetry.update();
                    sleep(2000);
                    telemetry.addLine("Autonomous done!");
                    telemetry.update();


                }
            }

        }

    }
