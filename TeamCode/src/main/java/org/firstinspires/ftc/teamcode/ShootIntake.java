package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ShootIntake {
    private DcMotorEx shooter1, shooter2;
    private Servo shooterAngle;
    private Servo shooterTrigger;
    private CRServo shooterFeeder1;
    private CRServo shooterFeeder2;
    private DcMotorEx intake;

    // Launch Sequence Variables
    private ElapsedTime launchTimer = new ElapsedTime();
    private ElapsedTime loadingTimer = new ElapsedTime();
    private boolean launching = false;
    private boolean resetingShot = false;
    private boolean loading = false;
    public boolean launchingSequenceRunning = false;

    public ShootIntake(HardwareMap hardwareMap) {
        shooterAngle = hardwareMap.get(Servo.class, "shooter angle");
        shooterTrigger = hardwareMap.get(Servo.class, "shooter trigger");
        shooterFeeder1 = hardwareMap.get(CRServo.class, "shooter feeder 1");
        shooterFeeder2 = hardwareMap.get(CRServo.class, "shooter feeder 2");

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter 1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter 2");

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.FORWARD);
    }


    // Set functions
    public void setShooterPower(double power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void setIntakePower(double power) {
        intake.setPower(power);
    }
    public void shooterTrigger() {
        shooterTrigger.setPosition(0.25);
    }

    public void shooterTriggerReset() {
        shooterTrigger.setPosition(1);
    }

    public void setShooterFeeder(double power) {
        shooterFeeder1.setPower(power);
        shooterFeeder2.setPower(-power);
    }

    public void setShooterAnglePosition(double position) {
        shooterAngle.setPosition(position);
    }


    // Launch Sequence Functions
    public void launchSequence() {
        if (launchTimer.seconds() >= 0.8 & launching) {
            shooterTriggerReset();
            resetingShot = true;
            launching = false;
            loadingTimer.reset();
        }

        if (loadingTimer.seconds() >= 0.5 & resetingShot) {
            setShooterFeeder(1);
            loading = true;
            resetingShot = false;
            loadingTimer.reset();
        }

        if (loadingTimer.seconds() >= 0.5 & loading) {
            setShooterFeeder(0);
            loading = false;
            launchingSequenceRunning = false;
        }

    }

    public void initiateLaunchSequence() {
        shooterTrigger();
        launching = true;
        launchTimer.reset();
    }
}
