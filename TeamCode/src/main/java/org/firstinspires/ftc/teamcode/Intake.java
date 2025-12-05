package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    public DcMotor intake;
    public Telemetry telemetry;
    public Servo sorter;
    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        intake = hardwareMap.get(DcMotor.class, "intake");
        sorter = hardwareMap.get(Servo.class, "sorter");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void intakeIn() {
        intake.setPower(1);
    }
    public void intakeStop(){
        intake.setPower(0);
    }
    public void sorterRight(){
        sorter.setPosition(0.5);
    }
    public void sorterLeft(){
        sorter.setPosition(0);
    }
}