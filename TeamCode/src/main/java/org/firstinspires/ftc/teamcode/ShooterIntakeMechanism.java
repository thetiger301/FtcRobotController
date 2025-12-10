package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterIntakeMechanism {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private HuskyLens huskyLens;
    private AprilTag apriltag;
    private Servo sorter;
    private DcMotor intake;
    private DcMotor shooter;
    private DcMotor angle;
    private String ballColor;
    private boolean purpleBallDetected;
    private boolean greenBallDetected;
    private double rollerPower = 0;
    public boolean colorIsBlue;
    public boolean isIntakeRunning;
    public boolean isShooterRunning;


    public ShooterIntakeMechanism(HardwareMap hardwareMap, Telemetry telemetry){
        //apriltag = new AprilTag(hardwareMap, telemetry);
        huskyLens = hardwareMap.get(HuskyLens.class, "Husky Lens");
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        intake = hardwareMap.get(DcMotor.class, "Intake");
        shooter = hardwareMap.get(DcMotor.class, "Shooter");
        angle = hardwareMap.get(DcMotor.class, "Angle");
        angle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
            } else if (blocks[i].id == 2 && blocks[i].x < 225) {
                ballColor = "Green";
                greenBallDetected = true;
                purpleBallDetected = false;
                break;
            } else{
                purpleBallDetected = false;
                greenBallDetected = false;
            }

            telemetry.addData("Block " + i, "ID: " + blocks[i].id + " X: " + blocks[i].x + " Y: " + blocks[i].y + "Color: " + ballColor);
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

    public void runShooter() {
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
        //if it is aligned, move on to find distance, set angle, and shoot
        else {
            if (apriltag.isBlueAligned) {
                //apriltag.get distance from blue apriltag()
                //angle.setTargetPosition(f(distance from apriltag));
            } else {
                //apriltag.get distance from red apriltag()
                //angle.setTargetPosition(f(distance from apriltag));
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
    }

    public void stopShooter () {
        apriltag.isBlueAligned = false;
        apriltag.isRedAligned = false;
        angle.setTargetPosition(0);
    }
}
