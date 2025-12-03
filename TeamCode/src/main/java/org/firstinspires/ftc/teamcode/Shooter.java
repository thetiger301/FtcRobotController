package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private DcMotor shooterMotor;
    private DcMotor shooterAngle;
    private Telemetry telemetry;
    public int shooterPosition = 0;
    public int currentShooterPosition = 0;
    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        shooterMotor = hardwareMap.get(DcMotor.class, "shooter");
        shooterAngle = hardwareMap.get(DcMotor.class, "shooter angle");
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterAngle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void runShooter() {
        shooterMotor.setPower(0.75);
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
}
