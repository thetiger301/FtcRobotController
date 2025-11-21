package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    public DcMotor intake;
    public Telemetry telemetry;
    public Gamepad gamepad1;
    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void intakeIn() {
        double intakePower = 1;
        intake.setPower(intakePower);
        telemetry.addData("Intake Power", intakePower);
    }
    public void intakeStop(){
        intake.setPower(0);
        telemetry.addData("Intake Power", 0);
    }
}
