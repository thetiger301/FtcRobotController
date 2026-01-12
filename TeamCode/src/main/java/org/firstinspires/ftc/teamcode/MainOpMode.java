package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public AprilTag aprilTag;
    public boolean fieldOriented = true;
    public double axial, lateral, yaw;
    public enum RobotState{
        MANUAL,
        INTAKE,
        SCORE
    }
    public RobotState robotState = RobotState.MANUAL;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        drivetrain.resetIMU();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Competition Program

            //Drivetrain control
            axial = -gamepad1.left_stick_y;
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;

            // Field oriented drive toggle
            if(gamepad1.dpadDownWasPressed()){
                fieldOriented = !fieldOriented;
            }

            // Defaults to fieldOriented true
            if (fieldOriented) {
                drivetrain.fieldOrientedDrive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", true);
            } else if (!fieldOriented) {
                drivetrain.drive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", false);
            }

            telemetry.addData("Status", "Running");
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.update();
        }
    }
}
