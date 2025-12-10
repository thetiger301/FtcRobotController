package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private DcMotor shooterMotor;
    private DcMotor shooterAngle;
    private DcMotor intake;
    public CRServo whiteFeeder;
    //public CRServo greyFeeder;

    private Telemetry telemetry;
    public int shooterPosition = 0;
    public int currentShooterPosition = 0;
    public double currentShooterPower = 0;
    public double shooterPower = 0;
    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter");
        shooterAngle = hardwareMap.get(DcMotor.class, "shooter angle");
        intake = hardwareMap.get(DcMotor.class, "intake");
        whiteFeeder = hardwareMap.get(CRServo.class, "white feeder");
        //greyFeeder = hardwareMap.get(CRServo.class, "grey feeder");
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterAngle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void runShooter() {
        shooterMotor.setPower(1);
        telemetry.addData("Shooter Velocity", shooterMotor.getPower());
    }

    public void stopShooter() {
        shooterMotor.setPower(0);
        telemetry.addData("Shooter Velocity", shooterMotor.getPower());
    }

    public void setShooterAngle(int targetTicks) {
        shooterAngle.setTargetPosition(targetTicks);
        shooterAngle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shooterAngle.setPower(0.3);
    }
    public void keepShooterAngle() {
        shooterAngle.setTargetPosition(currentShooterPosition);
        shooterAngle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shooterAngle.setPower(0.1);
    }
    public void setWhiteFeederPower(double power) {
        whiteFeeder.setPower(-power);
    }

    public void setGrayFeederPower(double power) {
        whiteFeeder.setPower(power);
    }
    public void runIntake() {
        intake.setPower(1);
    }
    public void stopIntake() {
        intake.setPower(0);
    }
}