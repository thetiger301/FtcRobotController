package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterIntakeMechanism {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private HuskyLens huskyLens;
    private AprilTag apriltag;
    private Servo sorter;
    private DcMotor intake;

    private DcMotor angle;
    private DcMotor shooter;
    public CRServo whiteFeeder;
    public CRServo grayFeeder;
    private String ballColor;
    private boolean purpleBallDetected;
    private boolean greenBallDetected;
    private double rollerPower = 0;
    private double whiteRollerPower = 0;
    public boolean colorIsBlue;
    public boolean isIntakeRunning;
    public boolean isShootProcessRunning;
    public boolean isWhiteFeederRunning;
    public boolean isGrayFeederRunning;
    public boolean isShooterMotorRunning;


    public ShooterIntakeMechanism(HardwareMap hardwareMap, Telemetry telemetry, AprilTag apriltag){
        this.apriltag = apriltag;
        this.telemetry = telemetry;
        huskyLens = hardwareMap.get(HuskyLens.class, "husky lens");
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotor.Direction.REVERSE);
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        angle = hardwareMap.get(DcMotor.class, "shooter angle");
        sorter = hardwareMap.get(Servo.class, "sorter");
        whiteFeeder = hardwareMap.get(CRServo.class, "white feeder");
        whiteFeeder.setDirection(DcMotor.Direction.REVERSE);
        grayFeeder = hardwareMap.get(CRServo.class, "gray feeder");
    }

    public void getHuskyLensData() {
        HuskyLens.Block[] blocks = huskyLens.blocks();


        for (int i = 0; i < blocks.length; i++) {
            // Access data fields for each block:
            // blocks[i].id: The learned ID of the object (e.g., 1, 2)
            // blocks[i].x, blocks[i].y: Center coordinates (origin top-left)
            // blocks[i].width, blocks[i].height: Size in pixels

            if (blocks[i].id == 1 && blocks[i].x < 225) {
                ballColor = "Purple";
                purpleBallDetected = true;
                greenBallDetected = false;
                break;
            } else if (blocks[i].id == 2 && blocks[i].x < 225 && blocks[i].x > 40) {
                ballColor = "Green";
                greenBallDetected = true;
                purpleBallDetected = false;
                break;
            } else{
                purpleBallDetected = false;
                greenBallDetected = false;
            }

            if(blocks[i].x <225){
                telemetry.addData("Block " + i, "ID: " + blocks[i].id + " X: " + blocks[i].x + " Y: " + blocks[i].y + "Color: " + ballColor);
            }
        }

        if (blocks.length == 0){
            purpleBallDetected = false;
            greenBallDetected = false;
        }
    }

    public void moveSorter(){
        getHuskyLensData();
        if(purpleBallDetected){
            sorter.setPosition(.65);
        } else if (greenBallDetected){
            sorter.setPosition(.23);
        } else{
            sorter.setPosition(.44);
        }
    }

    public void restSorter(){
        sorter.setPosition(.44);
    }

    public void runIntake(){
        moveSorter();
        intake.setPower(1);
    }

    public void stopIntake(){
        restSorter();
        intake.setPower(rollerPower);
    }

    public void reverseIntake(){
        intake.setPower(-1);
    }

    public void runShootProcess() {
        //if not aligned, align to apriltag
        if (!apriltag.isBlueAligned && !apriltag.isRedAligned) {
            //align for blue
            if (colorIsBlue) {
                apriltag.faceBlueAprilTag();
            }
            //align for red
            else {
                apriltag.faceRedAprilTag();
            }
        }
        //if it is aligned, move on to find distance
        if (apriltag.isBlueAligned && !apriltag.isAtTargetRange) {
            apriltag.driveTowardsBlueApriltag();
        } else if (apriltag.isRedAligned && !apriltag.isAtTargetRange) {
            apriltag.driveTowardsRedApriltag();
        }
        //if it is it at target range, shoot
        if (apriltag.isAtTargetRange){
            telemetry.addLine("Ready to Shoot");
        }
            //if (pattern 1){
                //green, purple, purple, complete
                //if(complete)
                    //isShooterRunning = false;
            //}
            //else if (pattern 2){
                //purple, green, purple, complete
                //if(complete)
                    //isShooterRunning = false;
            //}
            //else{
                //purple, purple, green, complete
                //if(complete)
                    //isShooterRunning = false;
            //}


    }

    public void stopShootProcess () {
        apriltag.isBlueAligned = false;
        apriltag.isRedAligned = false;
        apriltag.isAtTargetRange = false;
        //angle.setTargetPosition(0);
    }

    public void runShooterMotor(){
        shooter.setPower(.8);
    }

    public void stopShooterMotor() {
        shooter.setPower(0);
    }

    public void runWhiteFeeder(){
        whiteFeeder.setPower(1);
        whiteRollerPower = 0.5;
    }

    public void stopWhiteFeeder(){
        whiteFeeder.setPower(0);
        whiteRollerPower = 0;
    }

    public void runGrayFeeder(){
        grayFeeder.setPower(1);
        rollerPower = 0.5;
    }

    public void stopGrayFeeder(){
        grayFeeder.setPower(0);
        rollerPower = whiteRollerPower;
    }



}
