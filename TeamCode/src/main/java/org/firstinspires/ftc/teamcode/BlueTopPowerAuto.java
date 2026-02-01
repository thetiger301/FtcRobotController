package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name = "Blue Top Power Auto")
public class BlueTopPowerAuto extends LinearOpMode{

    private Drivetrain drivetrain;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);

        telemetry.addLine("Initialized");
        telemetry.update();

        // Wait for the start button
        waitForStart();
        drivetrain.resetIMU();

        if (opModeIsActive()) {
            drivetrain.strafeRight(.5);
            sleep(500);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //TODO add camera alignment and shooter code
            sleep(2200);
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
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
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
            //TODO add camera alignment and shooter code
            sleep(2200);
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
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
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
            //TODO add camera alignment and shooter code
            sleep(2200);
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
            //TODO Intake on
            sleep(1250);
            drivetrain.stopWheels();
            sleep(100);
            //TODO Intake off
            drivetrain.driveBackward();
            sleep(1350);
            drivetrain.stopWheels();
            sleep(100);
            drivetrain.strafeLeft(.7);
            sleep(2400);
            drivetrain.stopWheels();
            sleep(100);
            /*drivetrain.turnCounterClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
            //TODO add camera alignment and shooter code
            sleep(2200);
            /*drivetrain.turnClockwise();
            sleep(250);
            drivetrain.stopWheels();
            sleep(100);

             */
        }
    }
}