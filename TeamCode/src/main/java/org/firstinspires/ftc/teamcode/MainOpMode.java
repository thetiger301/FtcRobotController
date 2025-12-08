package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public AprilTag apriltag;
    public boolean fieldOriented;
    public double axial, lateral, yaw;
    public boolean ColorIsBlue;


    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        apriltag = new AprilTag(hardwareMap, telemetry);

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
            if(gamepad1.aWasPressed()){
                fieldOriented = !fieldOriented;
            }

            if (fieldOriented) {
                drivetrain.fieldOrientedDrive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", true);
            } else if (!fieldOriented) {
                drivetrain.drive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", false);
            }

            //Set Alliance Color
            if (gamepad1.dpadLeftWasPressed()){
                ColorIsBlue = !ColorIsBlue;
            }

            if (ColorIsBlue) {
                telemetry.addLine("Alliance Color is BLue");

            } else if (!ColorIsBlue) {
                telemetry.addLine("Alliance Color is Red");
            }

            if(gamepad1.leftBumperWasPressed()){
                if(ColorIsBlue){
                    apriltag.turningTowardsBlueApriltag = true;
                }
                else{
                    apriltag.turningTowardsRedApriltag = true;
                }
            }

            //Align to apriltag
            if (apriltag.turningTowardsBlueApriltag) {
                apriltag.faceBlueAprilTag();
            }
            if(apriltag.turningTowardsRedApriltag){
                apriltag.faceRedAprilTag();
            }
            if (gamepad1.x) {
                apriltag.giveBearing();
            }

            //override all aligning
            if(gamepad1.rightBumperWasPressed()){
                apriltag.turningTowardsBlueApriltag = false;
                apriltag.turningTowardsRedApriltag = false;
            }

            //Reset robot heading
            if(gamepad1.dpadUpWasPressed()){
                drivetrain.resetIMU();
            }

            if(gamepad1.y) {
                apriltag.getHuskyLensData();
            }

            telemetry.addData("Status", "Running");
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.update();
        }
    }
}
