package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Basic Auto")
public class AutonomousOp extends LinearOpMode {

    public Drivetrain drivetrain;
    public ElapsedTime opModeTimer;
    public ElapsedTime pathTimer;
    public ElapsedTime actionTimer;
    public enum Pattern {
        P_P_G,
        P_G_P,
        G_P_P
    }

    private enum BotState {
        START_TO_CAM_1,
        SHOOT_1
    }

    private BotState botState = BotState.START_TO_CAM_1;

    public void botStateUpdate() {
        switch (botState) {
            case START_TO_CAM_1:
                // drive towards the shoot position from
                drivetrain.driveForwardDistance(10, 0.5);
                drivetrain.turnToAngle(90, 0.5);
                botState = BotState.SHOOT_1;
                /* if (!drivetrain.frontLeft.isBusy() & !drivetrain.frontRight.isBusy()
                        & !drivetrain.backLeft.isBusy() & !drivetrain.backRight.isBusy() ) {
                    botState = BotState.SHOOT_1;
                }*/
                break;
            case SHOOT_1:
                // shoot code here
                break;
            default:
                drivetrain.stop();
                telemetry.addLine("No Bot State Selected");
        }
    }


    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        opModeTimer = new ElapsedTime();
        pathTimer = new ElapsedTime();
        actionTimer = new ElapsedTime();

        telemetry.addLine("Initialized");
        telemetry.update();

        // Wait for the start button
        waitForStart();
        drivetrain.resetHeading();
        opModeTimer.reset();

        while (opModeIsActive()){
            botStateUpdate();

            telemetry.update();
        }
    }
}