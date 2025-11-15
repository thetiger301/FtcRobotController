package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public boolean fieldOriented;
    public double axial, lateral, yaw;


    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry, gamepad1);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        drivetrain.imu.resetYaw();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Competition Program
            axial = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;

            if (gamepad1.aWasPressed() && !fieldOriented) {
                fieldOriented = true;
            }
            else if (gamepad1.aWasPressed() && fieldOriented){
                fieldOriented = false;
            }

            if (fieldOriented) {
                drivetrain.fieldOrientedDrive(axial, lateral, yaw);
            } else if (!fieldOriented) {
                drivetrain.drive(axial, lateral, yaw);
            }


            telemetry.addData("Status", "Running");
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.update();
        }
    }
}
