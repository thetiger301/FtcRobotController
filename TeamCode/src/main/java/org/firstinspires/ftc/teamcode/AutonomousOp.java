package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


    @Autonomous(name = "Basic Auto", group = "Autonomous")
    public class AutonomousOp extends LinearOpMode {

        private Drivetrain drivetrain;

        @Override
        public void runOpMode() {
            drivetrain = new Drivetrain(hardwareMap, telemetry);

            telemetry.addLine("Initialized");
            telemetry.update();

            // Wait for the start button
            waitForStart();
            drivetrain.resetIMU();

            if (opModeIsActive()) {
            }
        }
    }
