package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterIntakeMechanism {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private HuskyLens huskyLens;
    //private AprilTag apriltag;


    public ShooterIntakeMechanism(HardwareMap hardwareMap, Telemetry telemetry){
        //apriltag = new AprilTag(hardwareMap, telemetry);
        huskyLens = hardwareMap.get(HuskyLens.class, "Husky Lens");
        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
    }

    public void getHuskyLensData() {
        HuskyLens.Block[] blocks = huskyLens.blocks();


        for (int i = 0; i < blocks.length; i++) {
            // Access data fields for each block:
            // blocks[i].id: The learned ID of the object (e.g., 1, 2)
            // blocks[i].x, blocks[i].y: Center coordinates (origin top-left)
            // blocks[i].width, blocks[i].height: Size in pixels
            if (blocks[i].id == 1) {
                ballColor = "Purple";
            } else if (blocks[i].id == 2) {
                ballColor = "Green";
            }
            telemetry.addData("Block " + i, "ID: " + blocks[i].id + " X: " + blocks[i].x + " Y: " + blocks[i].y + "Color: " + color);
        }
    }


    public void runIntake(){
        
    }

    public void stopIntake(){

    }

    public void runShooter(){

    }

    public void stopShooter(){

    }




}
