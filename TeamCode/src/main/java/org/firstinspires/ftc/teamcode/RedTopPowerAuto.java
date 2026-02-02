package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Red Top Power Auto")
public class RedTopPowerAuto extends LinearOpMode{

    private Drivetrain drivetrain;
    private AutoDrive autoDrive;
    private ShootIntake shootIntake;
    private AprilTag aprilTag;
    private ElapsedTime shooterTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        autoDrive = new AutoDrive();
        shootIntake = new ShootIntake(hardwareMap);
        aprilTag = new AprilTag(hardwareMap);

        telemetry.addLine("Initialized");
        telemetry.update();

        // Wait for the start button
        waitForStart();
        drivetrain.resetIMU();

        if (opModeIsActive()) {
            shootIntake.shooterTriggerReset();
            drivetrain.strafeLeft(.5);
            sleep(500);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //TODO add camera alignment and shooter code

            shooterTimer.reset();
            shootIntake.waitingForLaunch = true;
            while (shooterTimer.milliseconds() <= 5000){
                //TODO change to blue tag
                aprilTag.readRedTag();
                // Run Launch Sequence and Set Shooter angle
                if(!autoDrive.isReadyToShoot){
                    double turnPower = autoDrive.autoAlign(aprilTag.getEffectiveBearing(), aprilTag.isValid(), 0);
                    drivetrain.setYaw(turnPower);
                }
                else{
                    drivetrain.setYaw(0);
                    shootIntake.launchSequence();
                }
            }
            shootIntake.endLaunchSequence();
            autoDrive.resetAlignment();

            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //begin first progression
            drivetrain.strafeLeft(.5);
            sleep(900);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeRight(.5);
            sleep(900);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

            */
            //TODO add camera alignment and shooter code
            sleep(2200);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //begin second progression
            drivetrain.strafeLeft(.6);
            sleep(1800);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeRight(.6);
            sleep(1800);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //TODO add camera alignment and shooter code
            sleep(2200);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //Begin third progression
            drivetrain.strafeLeft(.7);
            sleep(2400);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeRight(.7);
            sleep(2400);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //TODO add camera alignment and shooter code
            sleep(2200);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
        }
    }
}
