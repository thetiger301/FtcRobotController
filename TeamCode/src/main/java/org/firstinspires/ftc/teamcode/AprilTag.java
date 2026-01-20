package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTag {

    private Telemetry telemetry;
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private double bearing = 0;
    private double range = 0;
    private boolean tagVisible = false;
    private ElapsedTime detectionLostTimer = new ElapsedTime();
    public TagData redTagData = new TagData(tagVisible, bearing, range);


    public AprilTag(HardwareMap hardwareMap, Telemetry telemetry, Drivetrain drivetrain) {
        this.telemetry = telemetry;
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTagProcessor);
    }

    public void readRedTag() {
        AprilTagDetection targetTagRed = null;
        List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
        for (AprilTagDetection tag : detections) {
            if (tag.id == 24) {
                targetTagRed = tag;
                break;
            }
        }
        if (targetTagRed != null) {
            bearing = targetTagRed.ftcPose.bearing;  // degrees
            range = targetTagRed.ftcPose.range;
            tagVisible = true;
            detectionLostTimer.reset();
        } else {
            if (detectionLostTimer.milliseconds() <= 100) {
                return;
            } else {
                bearing = 0;
                range = 0;
                tagVisible = false;
            }
        }

        redTagData = new TagData(tagVisible, bearing, range);
    }

    public class TagData {
        public boolean tagVisible;
        public double tagBearingDeg;
        public double tagRangeIn;

        public TagData(boolean tagVisible, double tagBearingDeg, double tagRangeIn) {
            this.tagVisible = tagVisible;
            this.tagBearingDeg = tagBearingDeg;
            this.tagRangeIn = tagRangeIn;
        }
    }

}
