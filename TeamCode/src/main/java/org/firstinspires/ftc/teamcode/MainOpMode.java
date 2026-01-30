package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public ShootIntake shootIntake;
    public AutoDriveShoot autoDriveShoot;
    public AprilTag aprilTag;
    public boolean fieldOriented = true;
    public double axial, lateral, yaw;
    public double shooterPower = 0;
    public double currentShooterVelocity = 0;
    public double shooterVelocityError = 0;
    public double velocityTarget = 0;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        shootIntake = new ShootIntake(hardwareMap);
        aprilTag = new AprilTag(hardwareMap);
        autoDriveShoot = new AutoDriveShoot();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        drivetrain.resetIMU();
        shootIntake.shooterTriggerReset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Competition Program
            telemetry.addData("Status", "Running");

            //----Sensor Updates----
            currentShooterVelocity = shootIntake.getShooterVelocity();
            aprilTag.readRedTag();

            //----Gamepad Updates----

            //Drivetrain control
            axial = -gamepad1.left_stick_y;
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;

            // Field oriented drive toggle
            if (gamepad1.dpadDownWasPressed()){
                fieldOriented = !fieldOriented;
            }

            // Shoot Button (Hold)
            if (gamepad1.a) {
                shootIntake.launchSequenceRunning = true;
            } else {
                shootIntake.launchSequenceRunning = false;
            }
            if (gamepad1.aWasPressed()) {
                shootIntake.initiateLaunchSequence();
            }

            // Launch Zone Set Buttons (Press)
            if (gamepad2.xWasPressed()) {
                shootIntake.setCloseLaunchZone();
            }
            if (gamepad2.yWasPressed()) {
                shootIntake.setMidLaunchZone();
            }
            if (gamepad2.bWasPressed()) {
                shootIntake.setFarLaunchZone();
            }

            // Intake Button (Hold)
            if (gamepad1.x) {
                shootIntake.intaking = true;
            }
            if (gamepad1.xWasReleased()) {
                shootIntake.intaking = false;
                shootIntake.stopIntake();
            }

            // Auto Align Button (Hold)
            if (gamepad1.right_bumper) {
                autoDriveShoot.autoAlignEnabled = true;
            } else {
                autoDriveShoot.autoAlignEnabled = false;
            }

            //----Launch Sequence Logic----
            if (shootIntake.launchSequenceRunning) {
                velocityTarget = 1000;
                shooterVelocityError = shootIntake.getShooterVelocityError(velocityTarget);
                shootIntake.launchSequence(shooterVelocityError);
            } else {
                velocityTarget = 0;
                shootIntake.endLaunchSequence();
            }

            //----Motor and Servo Updates----

            // Run drivetrain
            if (fieldOriented) { // Defaults to fieldOriented true
                drivetrain.fieldOrientedDrive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", true);
            } else if (!fieldOriented) {
                drivetrain.drive(axial, lateral, yaw);
                telemetry.addData("Field Oriented Enabled", false);
            }

            // Run Shooter
            if (shootIntake.launchSequenceRunning) {
                shooterPower = shootIntake.getShooterPower(velocityTarget, currentShooterVelocity);
            } else {
                shooterPower = 0;
                shootIntake.resetShooterPID();
            }
            shootIntake.setShooterPower(shooterPower);

            // Run Intake
            if (shootIntake.intaking) {
                shootIntake.runIntake();
            }

            //----Telemetry Updates----
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", drivetrain.getHeading());
            telemetry.addData("Shooter Current Velocity", currentShooterVelocity);
            telemetry.addData(" Shooter Velocity Error", shooterVelocityError);
            telemetry.addLine(shootIntake.getCurrentLaunchZone());
            telemetry.addData("Detection Rate", aprilTag.getDetectionRate());
            telemetry.addData("Detection Confidence", aprilTag.getConfidence());
            telemetry.addData("Detection Valid", aprilTag.isValid());
            telemetry.update();
        }
    }
}
