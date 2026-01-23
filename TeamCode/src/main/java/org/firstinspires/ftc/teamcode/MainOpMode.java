package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Main OpMode")
public class MainOpMode extends LinearOpMode {

    // System Declarations
    public Shooter shooter;
    public double [] stepSizes = {1, 0.1, 0.01, 0.001, 0.0001};
    public int stepIndex = 1;
    public int highVelocity = 1000;
    public int lowVelocity = 0;
    public int curTargetVelocity = 0;

    @Override
    public void runOpMode() {
        shooter = new Shooter(hardwareMap);

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
                shooter.kPUp(stepSizes[stepIndex]);
            }

            if (gamepad1.dpadLeftWasPressed()) {
                shooter.kPDown(stepSizes[stepIndex]);
            }

            if (gamepad1.dpadUpWasPressed()) {
                shooter.motorPowerUp(stepSizes[stepIndex]);
            }

            if (gamepad1.dpadDownWasPressed()) {
                shooter.motorPowerDown(stepSizes[stepIndex]);
            }


            if (gamepad1.leftBumperWasPressed()) {
                curTargetVelocity = lowVelocity;
                shooter.resetPIDVelocity();
            }

            if (gamepad1.rightBumperWasPressed()) {
                curTargetVelocity = highVelocity;
                shooter.resetPIDVelocity();
            }

            shooter.motorPower = shooter.shooterPIDVelocity(curTargetVelocity, shooter.getShooterVelocity());
            shooter.setShooterPower(shooter.motorPower);

            
            if (gamepad1.bWasPressed()) {
                shooter.setShooterAnglePosition(0);
            } else if (gamepad1.yWasPressed()) {
                shooter.setShooterAnglePosition(0.5);
            }


            if (gamepad1.right_stick_button) {
                shooter.setIntakePower(1);
                shooter.setShooterFeeder(1);
            } else {
                shooter.setIntakePower(0);
                if (!shooter.launchingSequenceRunning) {
                    shooter.setShooterFeeder(0);
                }
            }





            telemetry.addData("Current Velocity", shooter.getShooterVelocity());
            telemetry.addData("Velocity Error", shooter.velocityError);
            telemetry.addData("Step Size", stepSizes[stepIndex]);
            telemetry.addData("Shooter Motor", shooter.motorPower);
            telemetry.addData("P Coefficient", shooter.kP);
            telemetry.addData("I Coefficient", shooter.kI);

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
