package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


    @Autonomous(name = "Basic Auto", group = "Autonomous")
    public class AutonomousOp extends LinearOpMode {

        public Drivetrain drivetrain;


        public double axial, lateral, yaw;

        @Override
        public void runOpMode() {
            drivetrain = new Drivetrain(hardwareMap, telemetry);


            telemetry.addLine("Initialized");
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.update();

            // Wait for the start button
            waitForStart();
            drivetrain.imu.resetYaw();


                if (opModeIsActive()) {

                    telemetry.addLine("Autonomous running...");
                    telemetry.update();

                    // Move forward 12 inches
                    drivetrain.driveForwardDistance(12, 0.5);
                    sleep(1000);


                    drivetrain.turnToAngle(90,0.5);


                    telemetry.addLine("Autonomous done!");
                    telemetry.update();


                }


        }

    }
