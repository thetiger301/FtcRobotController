package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Basic Auto", group = "Autonomous")
public class AutonomousOp extends LinearOpMode {

    private Drivetrain drivetrain;
    private AprilTag apriltag;
    private ShooterIntakeMechanism shooterIntakeMechanism;
    private AprilTag.Pattern pattern;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        apriltag = new AprilTag(hardwareMap, telemetry);
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

            drivetrain.driveForwardDistance(98, 0.8);
            //sleep(1000);
            drivetrain.turnToAngle(20,.6);
            sleep(500);
            apriltag.detectPattern();
            drivetrain.turnToAngle(-55,.6);
            switch (pattern) {
                case G_P_P:

                    break;
                case P_G_P:
                    break;
                case P_P_G:
                    break;
                default:
                    break;
            }
            drivetrain.turnToAngle(-90,.6);
            drivetrain.strafeDistance(-22, .8);

            shooterIntakeMechanism.setIntakePower(1);
            shooterIntakeMechanism.setSorterPosition(.65);
            drivetrain.driveForwardDistance(26,.4);
            sleep(500);
            drivetrain.driveForwardDistance(5,.4);
            sleep(500);
            shooterIntakeMechanism.setSorterPosition(.23);
            drivetrain.driveForwardDistance(8,.3);
            sleep(1000);
            drivetrain.driveForwardDistance(-39, .8);

            drivetrain.strafeDistance(22, .8);
            drivetrain.turnToAngle(-55, .6);

            //shoot
            switch (pattern) {
                case G_P_P:
                    break;
                case P_G_P:
                    break;
                case P_P_G:
                    break;
                default:
                    break;
            }

            telemetry.addLine("Autonomous done!");
            telemetry.update();
        }
    }
}
