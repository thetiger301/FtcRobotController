package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public Intake intake;
    public boolean fieldOriented;
    public double axial, lateral, yaw;


    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry, gamepad1);
        intake = new Intake(hardwareMap, gamepad1);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Competition Program

            telemetry.addData("Status", "Running");
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            axial = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;
            if (gamepad1.a) {
                fieldOriented = true;
            } else if (gamepad1.b){
                fieldOriented = false;
            }

            if (fieldOriented) {
                drivetrain.fieldOrientedDrive(axial, lateral, yaw);
            } else if (!fieldOriented) {
                drivetrain.drive(axial, lateral, yaw);
            }

            drivetrain.resetIMU();
            intake.intakeIn();

            telemetry.update();
        }
    }
}
