package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class Drivetrain {
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public Telemetry telemetry;
    public IMU imu;
    public Gamepad gamepad1;
    public Drivetrain(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1) {
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
        // Initialize motors with the same names from configuration
        frontLeft = hardwareMap.get(DcMotor.class, "front-left-drive");
        backLeft = hardwareMap.get(DcMotor.class, "back-left-drive");
        frontRight = hardwareMap.get(DcMotor.class, "front-right-drive");
        backRight = hardwareMap.get(DcMotor.class, "back-right-drive");

        // Set directions (same as in your current OpMode)
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
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
        telemetry.addData("Heading", botHeading);
    }
    public void resetIMU() {
        imu.resetYaw();
    }
}
