package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public AprilTag apriltag;
    public ShooterIntakeMechanism shooterIntakeMechanism;
    public boolean fieldOriented = true;
    public double axial, lateral, yaw;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        apriltag = new AprilTag(hardwareMap, telemetry, drivetrain);
        shooterIntakeMechanism = new ShooterIntakeMechanism(hardwareMap, telemetry, apriltag);

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
            if (gamepad1.aWasPressed()){
                shooterIntakeMechanism.isIntakeRunning = !shooterIntakeMechanism.isIntakeRunning;
            }
            if (shooterIntakeMechanism.isIntakeRunning) {
                shooterIntakeMechanism.runIntake();
            }
            if(!shooterIntakeMechanism.isIntakeRunning){
                shooterIntakeMechanism.stopIntake();
            }


            //turns the shooter mechanism on and off
            if(gamepad1.bWasPressed()){
                shooterIntakeMechanism.isShootProcessRunning = !shooterIntakeMechanism.isShootProcessRunning;
            }
            if(shooterIntakeMechanism.isShootProcessRunning){
                shooterIntakeMechanism.runShootProcess();
            }
            if(!shooterIntakeMechanism.isShootProcessRunning){
                shooterIntakeMechanism.stopShootProcess();
            }

            //turns the shooter motor on and off
            if(gamepad2.xWasPressed()){
                shooterIntakeMechanism.isShooterMotorRunning = !shooterIntakeMechanism.isShooterMotorRunning;
            }
            if(shooterIntakeMechanism.isShooterMotorRunning){
                shooterIntakeMechanism.runShooterMotor();
            }
            if(!shooterIntakeMechanism.isShooterMotorRunning){
                shooterIntakeMechanism.stopShooterMotor();
            }

            //turns the white feeder on and off
            if(gamepad2.bWasPressed()){
                shooterIntakeMechanism.isWhiteFeederRunning = !shooterIntakeMechanism.isWhiteFeederRunning;
            }
            if(shooterIntakeMechanism.isWhiteFeederRunning){
                shooterIntakeMechanism.runWhiteFeeder();
            }
            if(!shooterIntakeMechanism.isWhiteFeederRunning){
                shooterIntakeMechanism.stopWhiteFeeder();
            }

            //turns the gray feeder on and off
            if(gamepad2.aWasPressed()){
                shooterIntakeMechanism.isGrayFeederRunning = !shooterIntakeMechanism.isGrayFeederRunning;
            }
            if(shooterIntakeMechanism.isGrayFeederRunning){
                shooterIntakeMechanism.runGrayFeeder();
            }
            if(!shooterIntakeMechanism.isGrayFeederRunning){
                shooterIntakeMechanism.stopGrayFeeder();
            }

            //tua
            if (gamepad1.y){
                shooterIntakeMechanism.reverseIntake();
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
