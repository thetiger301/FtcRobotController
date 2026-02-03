package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Blue Top Power Auto")
public class BlueTopPowerAuto extends LinearOpMode{

    private Drivetrain drivetrain;
    private BlueAutoDrive autoDrive;
    private ShootIntake shootIntake;
    private AprilTag aprilTag;
    private ElapsedTime shooterTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        autoDrive = new BlueAutoDrive();
        shootIntake = new ShootIntake(hardwareMap);
        aprilTag = new AprilTag(hardwareMap);


        telemetry.addLine("Initialized");
        telemetry.update();

        // Wait for the start button
        waitForStart();
        drivetrain.resetIMU();

        if (opModeIsActive()) {
            shootIntake.shooterTriggerReset();
            drivetrain.strafeRight(.5);
            sleep(500);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            shooterTimer.reset();
            shootIntake.initiateLaunchSequence();
            while (shooterTimer.milliseconds() <= 5000){
                aprilTag.readBlueTag();
                // Run Launch Sequence and Set Shooter angle
                if(!autoDrive.isReadyToShoot){
                    double turnPower = autoDrive.autoAlign(aprilTag.getEffectiveBearing(), aprilTag.isValid(), 0);
                    drivetrain.setYaw(turnPower);
                }
                else{
                    drivetrain.setYaw(0);
                    shootIntake.launchSequence(aprilTag.getLastRange());
                }
            }
            shootIntake.endLaunchSequence();
            autoDrive.resetAlignment();

            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //begin first progression
            drivetrain.strafeRight(.5);
            sleep(900);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            shootIntake.runIntake();
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            shootIntake.stopIntake();
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeLeft(.5);
            sleep(900);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

            */
            shooterTimer.reset();
            shootIntake.initiateLaunchSequence();
            while (shooterTimer.milliseconds() <= 5000){
                aprilTag.readBlueTag();
                // Run Launch Sequence and Set Shooter angle
                if(!autoDrive.isReadyToShoot){
                    double turnPower = autoDrive.autoAlign(aprilTag.getEffectiveBearing(), aprilTag.isValid(), 0);
                    drivetrain.setYaw(turnPower);
                }
                else{
                    drivetrain.setYaw(0);
                    shootIntake.launchSequence(aprilTag.getLastRange());
                }
            }
            shootIntake.endLaunchSequence();
            autoDrive.resetAlignment();
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //begin second progression
            drivetrain.strafeRight(.6);
            sleep(1800);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            shootIntake.runIntake();
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            shootIntake.stopIntake();
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeLeft(.6);
            sleep(1800);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            shooterTimer.reset();
            shootIntake.initiateLaunchSequence();
            while (shooterTimer.milliseconds() <= 5000){
                aprilTag.readBlueTag();
                // Run Launch Sequence and Set Shooter angle
                if(!autoDrive.isReadyToShoot){
                    double turnPower = autoDrive.autoAlign(aprilTag.getEffectiveBearing(), aprilTag.isValid(), 0);
                    drivetrain.setYaw(turnPower);
                }
                else{
                    drivetrain.setYaw(0);
                    shootIntake.launchSequence(aprilTag.getLastRange());
                }
            }
            shootIntake.endLaunchSequence();
            autoDrive.resetAlignment();
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //Begin third progression
            drivetrain.strafeRight(.7);
            sleep(2400);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.driveForward();
            shootIntake.runIntake();
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            shootIntake.stopIntake();
            sleep(10000);
        }
    }
}
