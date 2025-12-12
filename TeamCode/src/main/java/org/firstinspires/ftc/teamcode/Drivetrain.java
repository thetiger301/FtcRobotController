package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Drivetrain {
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private Telemetry telemetry;
    private IMU imu;
    private double kP = 0.04;
    private double errorTolerance = 2;
    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        // Initialize motors with the same names from configuration
        frontLeft = hardwareMap.get(DcMotor.class, "front-left-drive");
        backLeft = hardwareMap.get(DcMotor.class, "back-left-drive");
        frontRight = hardwareMap.get(DcMotor.class, "front-right-drive");
        backRight = hardwareMap.get(DcMotor.class, "back-right-drive");

        //put the motors in break mode
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set directions (same as in your current OpMode)
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        imu.initialize(parameters);
    }

    public double getHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void drive(double axial, double lateral, double yaw) {
        double max;

        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

        // Normalize so no value exceeds 1.0
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        // Apply powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
    }

    public void fieldOrientedDrive(double axial, double lateral, double yaw) {
        double max;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = axial * Math.cos(botHeading) - lateral * Math.sin(botHeading);
        double rotY = axial * Math.sin(botHeading) + lateral * Math.cos(botHeading);

        double frontLeftPower  = rotX + rotY + yaw;
        double frontRightPower = rotX - rotY - yaw;
        double backLeftPower   = rotX - rotY + yaw;
        double backRightPower  = rotX + rotY - yaw;

        // Normalize so no value exceeds 1.0
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

        // Apply powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);



        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
    }

    // reset heading
    public void resetIMU() {
        imu.resetYaw();
    }

    //for autonomous
    public void driveForwardDistance(double inches, double power) {
        int ticksPerRev = 537; // GoBILDA 312 RPM motor; adjust for yours
        double wheelDiameter = 3.779; // in inches (96mm GoBILDA wheels)
        double ticksPerInch = ticksPerRev / (wheelDiameter * Math.PI);

        int targetTicks = (int) (inches * ticksPerInch);

        // Reset encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set target position
        frontLeft.setTargetPosition(targetTicks);
        frontRight.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(targetTicks);
        backRight.setTargetPosition(targetTicks);

        // Set to RUN_TO_POSITION mode
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        // Wait until all motors are done
        while (frontLeft.isBusy() && frontRight.isBusy() &&
                backLeft.isBusy() && backRight.isBusy()) {
            // You can add telemetry here to show progress
        }

        // Stop all motion
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Return to encoder mode
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void turnToAngle(double targetAngle, double power) {

        double error = targetAngle - getHeading();

        while (Math.abs(error) > 10) {   // stop when within ±1 degree
            double turnPower = error * 0.015; // slow down as you get close
            turnPower = Math.max(-power, Math.min(power, turnPower));

            // turn robot
            frontLeft.setPower(-turnPower);
            backLeft.setPower(-turnPower);
            frontRight.setPower(turnPower);
            backRight.setPower(turnPower);

            // recalc error
            error = targetAngle - getHeading();

        }

        stop();
    }

    public void turnThisManyDegrees(double targetAngle, double power) {

        double targetHeading = targetAngle + getHeading();
        double error = targetHeading - getHeading();

        while (Math.abs(error) > errorTolerance) {   // stop when within ±1 degree

            double turnPower = error * kP; // slow down as you get close
            turnPower = Math.max(-power, Math.min(power, turnPower));

            // turn robot
            frontLeft.setPower(-turnPower);
            backLeft.setPower(-turnPower);
            frontRight.setPower(turnPower);
            backRight.setPower(turnPower);

            // recalc error
            if (-120 > getHeading() && getHeading() > -180 && targetHeading >= 180){
                targetHeading = targetHeading - 360;
            }
            if (120 < getHeading() && getHeading() <= 180 && targetHeading <= -180){
                targetHeading = targetHeading + 360;
            }
            error = targetHeading - getHeading();

        }

        stop();
    }

    public void strafeDistance(double inches, double power) {
        int ticksPerRev = 537; // adjust for your motor
        double wheelDiameter = 3.779; // inches
        double ticksPerInch = ticksPerRev / (wheelDiameter * Math.PI);

        int targetTicks = (int)(inches * ticksPerInch);

        // Reset encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Mecanum strafe: each wheel moves in a different direction
        frontLeft.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(-targetTicks);
        frontRight.setTargetPosition(-targetTicks);
        backRight.setTargetPosition(targetTicks);

        // RUN_TO_POSITION mode
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        // Wait for completion
        while (frontLeft.isBusy() && frontRight.isBusy() &&
                backLeft.isBusy() && backRight.isBusy()) {
            // optional telemetry
        }

        stop();

        // Return to normal mode
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //for autonomous
    public void forward(double power){
        drive(power,0,0);
    }

    public void strafe(double power){
        drive(0,power,0);
    }

    public void turn(double power){
        drive(0,0,power);
    }

    public void stop() {
        drive(0, 0, 0);
    }
}
