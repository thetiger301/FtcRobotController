package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Red Back", group = "Autonomous")
public class RedBackAuto extends LinearOpMode {
    private Drivetrain drivetrain;
    private AprilTag apriltag;
    private ShooterIntakeMechanism shooterIntakeMechanism;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry, gamepad1);
        apriltag = new AprilTag(hardwareMap, telemetry, drivetrain);
        shooterIntakeMechanism = new ShooterIntakeMechanism(hardwareMap, telemetry, apriltag);

        telemetry.addLine("Initialized");
        telemetry.addData("Heading", drivetrain.getHeading());
        telemetry.update();

        // Wait for the start button
        waitForStart();
        drivetrain.resetIMU();

        if (opModeIsActive()) {
            telemetry.addLine("Autonomous running...");
            telemetry.update();

            shooterIntakeMechanism.runAutoShooterMotor();
            drivetrain.driveForwardDistance(88, 0.8);
            sleep(300);
            drivetrain.turnToAngle(25, .6);
            sleep(500);
            apriltag.detectPattern();
            sleep(100);
            apriltag.detectPattern();
            sleep(100);
            apriltag.detectPattern();
            sleep(100);
            apriltag.detectPattern();
            sleep(100);
            drivetrain.turnToAngle(-55, .6);
            sleep(800);
            apriltag.faceRedAprilTag();
            sleep(200);
            apriltag.driveTowardsRedApriltag();
            sleep(3000);
            switch (apriltag.pattern) {
                case 0:
                    // G_P_P
                    shooterIntakeMechanism.autoGrayFeeder(1);
                    shooterIntakeMechanism.autoIntake(0.5);
                    sleep(4000);
                    shooterIntakeMechanism.autoWhiteFeeder(1);
                    sleep(2500);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    sleep(1000);
                    shooterIntakeMechanism.autoWhiteFeeder(1);
                    sleep(4000);
                    shooterIntakeMechanism.autoGrayFeeder(0);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    shooterIntakeMechanism.autoIntake(0);
                    break;
                case 1:
                    // P_G_P
                    shooterIntakeMechanism.autoWhiteFeeder(1);
                    shooterIntakeMechanism.autoIntake(0.5);
                    sleep(4000);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    shooterIntakeMechanism.autoGrayFeeder(0.5);
                    sleep(4000);
                    shooterIntakeMechanism.autoGrayFeeder(0);
                    shooterIntakeMechanism.autoWhiteFeeder(0.5);
                    sleep(4000);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    shooterIntakeMechanism.autoIntake(0);
                    break;
                case 2:
                    // P_P_G
                    shooterIntakeMechanism.autoWhiteFeeder(1);
                    shooterIntakeMechanism.autoIntake(0.5);
                    sleep(2500);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    sleep(1000);
                    shooterIntakeMechanism.autoWhiteFeeder(1);
                    sleep(3000);
                    shooterIntakeMechanism.autoGrayFeeder(1);
                    sleep(4000);
                    shooterIntakeMechanism.autoGrayFeeder(0);
                    shooterIntakeMechanism.autoWhiteFeeder(0);
                    shooterIntakeMechanism.autoIntake(0);
                    break;
            }


            drivetrain.turnToAngle(95, .6);
                /*drivetrain.strafeDistance(-22, .8);

                shooterIntakeMechanism.autoIntake(1);
                shooterIntakeMechanism.setSorterPosition(.65);
                drivetrain.driveForwardDistance(26,.4);
                sleep(500);
                drivetrain.driveForwardDistance(5,.4);
                sleep(500);
                shooterIntakeMechanism.setSorterPosition(.23);
                drivetrain.driveForwardDistance(8,.3);
                sleep(1000);
                shooterIntakeMechanism.autoIntake(0);
                drivetrain.driveForwardDistance(-39, .8);

                drivetrain.strafeDistance(22, .8);
                drivetrain.turnToAngle(-55, .6);
                sleep(1000);

                //shoot
                switch (pattern) {
                    case G_P_P:
                        shooterIntakeMechanism.autoGrayFeeder(1);
                        shooterIntakeMechanism.autoIntake(0.5);
                        sleep(3000);
                        shooterIntakeMechanism.autoWhiteFeeder(1);
                        sleep(6000);
                        shooterIntakeMechanism.autoGrayFeeder(0);
                        shooterIntakeMechanism.autoWhiteFeeder(0);
                        shooterIntakeMechanism.autoIntake(0);
                        break;
                    case P_G_P:
                        shooterIntakeMechanism.autoWhiteFeeder(1);
                        shooterIntakeMechanism.autoIntake(0.5);
                        sleep(3000);
                        shooterIntakeMechanism.autoWhiteFeeder(0);
                        shooterIntakeMechanism.autoGrayFeeder(0.5);
                        sleep(3000);
                        shooterIntakeMechanism.autoGrayFeeder(0);
                        shooterIntakeMechanism.autoWhiteFeeder(0.5);
                        sleep(3000);
                        shooterIntakeMechanism.autoWhiteFeeder(0);
                        shooterIntakeMechanism.autoIntake(0);
                        break;
                    case P_P_G:
                        shooterIntakeMechanism.autoWhiteFeeder(1);
                        shooterIntakeMechanism.autoIntake(0.5);
                        sleep(6000);
                        shooterIntakeMechanism.autoGrayFeeder(1);
                        sleep(3000);
                        shooterIntakeMechanism.autoGrayFeeder(0);
                        shooterIntakeMechanism.autoWhiteFeeder(0);
                        shooterIntakeMechanism.autoIntake(0);
                        break;
                }

                 */

            telemetry.addLine("Autonomous done");
            telemetry.update();
        }
    }
}
