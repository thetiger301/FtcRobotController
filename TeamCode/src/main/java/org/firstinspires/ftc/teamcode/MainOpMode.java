package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Shooter shooter;
    public boolean powerOn = false;
    public double [] stepSizes = {10, 1, 0.1, 0.01, 0.001};
    public int stepIndex = 1;
    public double F = 10;
    public double P = 0;
    public int highVelocity = 2500;
    public int lowVelocity = 1000;
    public int curTargetVelocity = 0;


    @Override
    public void runOpMode() {
        shooter = new Shooter(hardwareMap, F, P);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.xWasPressed()) {
                stepIndex = (stepIndex + 1) % stepSizes.length;
            }

            if (gamepad1.dpadRightWasPressed()) {
                F += stepSizes[stepIndex];
            }

            if (gamepad1.dpadRightWasPressed()) {
                F -= stepSizes[stepIndex];
            }

            if (gamepad1.dpadUpWasPressed()) {
                P += stepSizes[stepIndex];
            }

            if (gamepad1.dpadDownWasPressed()) {
                P -= stepSizes[stepIndex];
            }

            if (gamepad1.rightBumperWasPressed()) {
                curTargetVelocity = highVelocity;
            }

            if (gamepad1.leftBumperWasPressed()) {
                curTargetVelocity = lowVelocity;
            }


            if (gamepad1.aWasPressed()) {
                if (!powerOn) {
                    shooter.setShooterPower(1);
                    powerOn = true;
                } else if(powerOn) {
                    shooter.setShooterPower(0);
                    powerOn = false;
                }
            }

            if (gamepad1.bWasPressed()) {
                shooter.setShooterAnglePosition(0);
            } else if (gamepad1.yWasPressed()) {
                shooter.setShooterAnglePosition(0.5);
            }

            shooter.setShooterVelocity(curTargetVelocity, P, F);

            telemetry.addData("Current Velocity", shooter.getShooterVelocity());
            telemetry.addData("Step Size", stepSizes[stepIndex]);
            telemetry.addData("F Coefficient", F);
            telemetry.addData("P Coefficient", P);

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
