package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Red Main OpMode")
public class RedMainOpMode extends LinearOpMode {

    // System Declarations
    public Drivetrain drivetrain;
    public ShootIntake shootIntake;
    public RedAutoDrive autoDrive;
    public AprilTag aprilTag;
    public double axial, lateral, yaw;
    public double currentHeading = 0;
    public double currentAngularVelocity = 0;
    public double currentShooterVelocity = 0;
    public double aprilTagBearing = 0;
    public double aprilTagEffectiveBearing = 0;
    public double aprilTagRange = 0;
    public boolean isAprilTagValid = false;
    public double aprilTagConfidence = 0;
    public double aprilTagDetectionRate = 0;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        shootIntake = new ShootIntake(hardwareMap);
        aprilTag = new AprilTag(hardwareMap);
        autoDrive = new RedAutoDrive();

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
            //TODO ADD BLUE TEAM OR RED TEAM SWITCHER IF STATEMENT
            aprilTag.readRedTag();
            aprilTagBearing = aprilTag.getLastBearing();
            aprilTagEffectiveBearing = aprilTag.getEffectiveBearing();
            aprilTagRange = aprilTag.getLastRange();
            isAprilTagValid = aprilTag.isValid();
            aprilTagDetectionRate = aprilTag.getDetectionRate();
            aprilTagConfidence = aprilTag.getConfidence();
            currentShooterVelocity = shootIntake.getShooterVelocity();
            currentHeading = drivetrain.getHeading();
            currentAngularVelocity = drivetrain.getAngularVelocity();

            //----Main Program----

            //Drivetrain control
            axial = -gamepad1.left_stick_y;
            lateral = gamepad1.left_stick_x;
            yaw = gamepad1.right_stick_x;


            // Align and Shoot Button (Hold)
            if (gamepad1.a) {
                // Run Launch Sequence and Set Shooter angle
                if(!autoDrive.isReadyToShoot){
                    yaw = autoDrive.autoAlign(aprilTagEffectiveBearing, isAprilTagValid, yaw);
                } else {
                    shootIntake.launchSequence(aprilTagRange);
                }
            }
            else {
                // ends and resets alignment and shooter
                autoDrive.resetAlignment();
                shootIntake.endLaunchSequence();
            }

            //sets important variable to true that will turn off as soon as shooter is up to velocity
            if (gamepad1.aWasPressed()) {
                shootIntake.initiateLaunchSequence();
            }


            //set drivetrain motors to robot oriented
            drivetrain.drive(axial, lateral, yaw);


            // Intake Button (Hold)
            if (gamepad1.x) {
                // Run Intake
                shootIntake.runIntake();
            }
            if (gamepad1.xWasReleased()) {
                // Stop Intake
                shootIntake.stopIntake();
            }


            // Launch Zone Set Buttons (Change to Gamepad 2) (Press)
            if (gamepad1.dpadLeftWasPressed()) {
                shootIntake.setCloseLaunchZone();
            }
            if (gamepad1.dpadUpWasPressed()) {
                shootIntake.setMidLaunchZone();
            }
            if (gamepad1.dpadRightWasPressed()) {
                shootIntake.setFarLaunchZone();
            }


            //----Telemetry Updates----
            telemetry.addLine("Driver Data");
            telemetry.addData("Detection Valid", isAprilTagValid);
            telemetry.addData("Inputs", "axial: %.2f, lateral: %.2f, yaw: %.2f", axial, lateral, yaw);
            telemetry.addData("Heading", currentHeading);
            telemetry.addLine(shootIntake.getCurrentLaunchZone());
            telemetry.addData("Shooter Current Velocity", currentShooterVelocity);
            telemetry.addLine();
            telemetry.addLine("Camera Data");
            telemetry.addData("Bearing", aprilTagBearing);
            telemetry.addData("Range", aprilTagRange);
            telemetry.addData("Effective Bearing", aprilTag.getEffectiveBearing());
            telemetry.addData("Detection Rate", aprilTagDetectionRate);
            telemetry.addData("Detection Confidence", aprilTagConfidence);
            telemetry.update();
        }
    }
}
