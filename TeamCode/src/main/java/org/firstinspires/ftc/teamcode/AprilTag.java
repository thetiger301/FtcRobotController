package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
public class AprilTag {
    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTagProcessor;
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Drivetrain drivetrain;

    public AprilTag(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTagProcessor);
    }

    public void giveRange() {
        AprilTagDetection targetTagRed = null;
        AprilTagDetection targetTagBlue = null;
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        for (AprilTagDetection tag : detections) {
            if (tag.id == 24) {
                targetTagRed = tag;
                break;  // Stop looping once we find it
            } else if (tag.id == 20) {
                targetTagBlue = tag;
                break;
            }
        }
        if (targetTagRed != null) {
            double range = targetTagRed.ftcPose.range;  // degrees
            telemetry.addData("Target Tag", "Red Tag");
            telemetry.addData("Range", "%.1f", range);
        } else if (targetTagBlue != null) {
            double range = targetTagBlue.ftcPose.range;  // degrees
            telemetry.addData("Target Tag", "Blue Tag");
            telemetry.addData("Range", "%.1f", range);
        } else {
            telemetry.addLine("Tag not Detected");
        }
    }
}