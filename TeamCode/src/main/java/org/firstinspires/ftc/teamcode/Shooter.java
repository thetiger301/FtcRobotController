package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private DcMotor shooter;
    private Telemetry telemetry;
    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

    }
    public void runShooter() {
        double shooterPower = 0.5;
        shooter.setPower(shooterPower);
        telemetry.addData("Shooter Power", shooterPower);
    }
}
