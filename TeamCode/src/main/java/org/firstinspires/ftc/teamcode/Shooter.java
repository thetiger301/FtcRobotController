package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class Shooter {
    private DcMotorEx shooter1, shooter2;
    private Servo shooterAngle;

    public Shooter(HardwareMap hardwareMap, double F, double P) {
        shooterAngle = hardwareMap.get(Servo.class, "shooter angle");

        shooter1 = hardwareMap.get(DcMotorEx.class, "shooter 1");
        shooter2 = hardwareMap.get(DcMotorEx.class, "shooter 2");

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        shooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setShooterPower(double power) {
        shooter1.setPower(power);
        shooter2.setPower(power);
    }

    public void setShooterVelocity(double velocity, double F, double P) {
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        shooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        shooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        shooter1.setVelocity(velocity);
        shooter2.setVelocity(velocity);
    }

    public double getShooterVelocity() {
        double velocity = shooter1.getVelocity();
        return velocity;
    }

    public void setShooterAnglePosition(double position) {
        shooterAngle.setPosition(position);
    }
}
