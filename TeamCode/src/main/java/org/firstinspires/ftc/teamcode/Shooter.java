package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class Shooter {
    private DcMotorEx shooter1, shooter2;
    private Servo shooterAngle;
    private Servo shooterTrigger;
    private CRServo shooterFeeder1;
    private CRServo shooterFeeder2;
    private DcMotorEx intake;
    private double lastVelocity = 0;
    public double velocityError = 0;
    private double derivative = 0;
    private double integralSum = 0;
    private double feedFoward = 0;
    ElapsedTime shooterPIDTimer = new ElapsedTime();
    public double dt = 0;
    public double kP = 0.013;
    public double kD = 0;
    public double kI = 0;
    public double kV = 0.0005;
    public double kS = 0.1;
    private double commandedVelocity = 0;
    private double maxAccel = 3000;
    private double maxDecel = 1500;
    public double motorPower = 0;

    public Shooter(HardwareMap hardwareMap) {
        shooterAngle = hardwareMap.get(Servo.class, "shooter angle");
        shooterTrigger = hardwareMap.get(Servo.class, "shooter trigger");
        shooterFeeder1 = hardwareMap.get(CRServo.class, "shooter feeder 1");
        shooterFeeder2 = hardwareMap.get(CRServo.class, "shooter feeder 2");

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter 1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter 2");

        shooter1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setShooterPower(double power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void setIntakePower(double power) {
        intake.setPower(power);
    }

    public double shooterPIDVelocity(double targetVelocity, double currentVelocity) {
        // Loop time
        dt = shooterPIDTimer.seconds();
        if (dt <= 0) {
            dt = 1e-3;
        }

        // Acceleration rate clamp
        double delta = targetVelocity - commandedVelocity;
        double maxDelta = (delta > 0 ? maxAccel : maxDecel) * dt;
        delta = Range.clip(delta, -maxDelta, maxDelta);
        commandedVelocity += delta;

        // Error Calculation
        velocityError = commandedVelocity - currentVelocity;

        // Derivative
        derivative = (currentVelocity - lastVelocity) / dt;

        //Integral
        integralSum = integralSum + (velocityError * dt);
        integralSum = Range.clip(integralSum, -5000, 5000);

        // Feedfoward
        feedFoward = kV * commandedVelocity;
        if (Math.abs(commandedVelocity) > 50) {
            feedFoward += kS * Math.signum(commandedVelocity);
        }

        double output = (kP * velocityError) + (kD * derivative) + (kI * integralSum) + (feedFoward);

        lastVelocity = currentVelocity;
        shooterPIDTimer.reset();

        double finalOutput = Range.clip(output, -1, 1);
        return finalOutput;
    }

    public void resetPIDVelocity() {
        derivative = 0;
        integralSum = 0;
        commandedVelocity = 0;
        shooterPIDTimer.reset();
    }

    public void kPUp(double change) {
        kP += change;
    }

    public void kPDown(double change) {
        kP -= change;
    }

    public void kIUp(double change) {
        kI += change;
    }

    public void kIDown(double change) {
        kI -= change;
    }


    public void motorPowerUp(double change) {
        motorPower += change;
    }

    public void motorPowerDown(double change) {
        motorPower -= change;
    }



    public double getShooterVelocity() {
        double velocity = shooter1.getVelocity();
        return velocity;
    }

    public void shoot(double position) {
        shooterTrigger.setPosition(position);
    }

    public void setShooterAnglePosition(double position) {
        shooterAngle.setPosition(position);
    }

    public void setShooterFeeder(double power) {
        shooterFeeder1.setPower(power);
        shooterFeeder2.setPower(-power);
    }
}
