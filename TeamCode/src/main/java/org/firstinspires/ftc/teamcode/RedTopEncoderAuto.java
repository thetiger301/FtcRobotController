package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Red Top Encoder Auto")
public class RedTopEncoderAuto extends LinearOpMode {
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
            drivetrain.strafeDistance(-6, .5);
            sleep(500);
            //drivetrain.turnToAngle(20, .5);
            sleep(500);
            //TODO add shooter code
            //drivetrain.turnToAngle(0,.5);
            sleep(500);
            drivetrain.strafeDistance(-21,.8);
            sleep(500);
            drivetrain.driveForwardDistance(32, .8);
            sleep(500);
            drivetrain.driveForwardDistance(-32, .8);
            sleep(500);
            drivetrain.strafeDistance(21,.8);
            sleep(500);
            //drivetrain.turnToAngle(20,.5);
            sleep(500);
            //TODO add shooter code
            //drivetrain.turnToAngle(0,.5);
            sleep(500);
            drivetrain.strafeDistance(-45,.8);
            sleep(500);
            drivetrain.driveForwardDistance(32, .8);
            sleep(500);
            drivetrain.driveForwardDistance(-32, .8);
            sleep(500);
            drivetrain.strafeDistance(45,.8);
            sleep(500);
            //drivetrain.turnToAngle(20,.5);
            sleep(500);
            //TODO add shooter code
            //drivetrain.turnToAngle(0,.5);
            sleep(500);
            drivetrain.strafeDistance(-69,.8);
            sleep(500);
            drivetrain.driveForwardDistance(32, .8);
            sleep(500);
            drivetrain.driveForwardDistance(-32, .8);
            sleep(500);
            drivetrain.strafeDistance(69,.8);
            sleep(500);
            //drivetrain.turnToAngle(20,.5);
            sleep(500);
            //TODO add shooter code
        }
    }
}