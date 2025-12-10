package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public AprilTag apriltag;
    public ShooterIntakeMechanism shooterIntakeMechanism;
    public boolean fieldOriented;
    public double axial, lateral, yaw;




    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        apriltag = new AprilTag(hardwareMap, telemetry);
        shooterIntakeMechanism = new ShooterIntakeMechanism(hardwareMap, telemetry);


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
                shooterIntakeMechanism.colorIsBlue = !shooterIntakeMechanism.colorIsBlue;
            }
            if (shooterIntakeMechanism.colorIsBlue) {
                telemetry.addLine("Alliance Color Blue");
            } else{
                telemetry.addLine("Alliance Color Red");
            }

            //turns the intake on and off
            if (gamepad1.leftStickButtonWasPressed()){
                shooterIntakeMechanism.isIntakeRunning = !shooterIntakeMechanism.isIntakeRunning;
            }
            if (shooterIntakeMechanism.isIntakeRunning) {
                shooterIntakeMechanism.runIntake();
            }
            if(!shooterIntakeMechanism.isIntakeRunning){
                shooterIntakeMechanism.stopIntake();
            }

            //turns the shooter on and off
            if(gamepad1.rightStickButtonWasPressed()){
                shooterIntakeMechanism.isShooterRunning = !shooterIntakeMechanism.isShooterRunning;
            }
            if(shooterIntakeMechanism.isShooterRunning){
                shooterIntakeMechanism.runShooter();
            }
            if(!shooterIntakeMechanism.isShooterRunning){
                shooterIntakeMechanism.stopShooter();
            }

            //give the bearing
            if (gamepad1.x) {
                apriltag.giveBearing();
            }

            //Reset robot heading
            if(gamepad1.dpadUpWasPressed()){
                drivetrain.resetIMU();
            }

            telemetry.addData("Status", "Running");
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.update();
        }
    }
}
