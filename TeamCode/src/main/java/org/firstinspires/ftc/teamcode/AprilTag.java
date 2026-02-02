package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayDeque;
import java.util.List;

public class AprilTag {

    private Telemetry telemetry;
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private double lastBearing = 0;
    private double lastRange = 0;
    private double confidence = 0;

    // Sliding Window Detection Time Stamps
    private final ArrayDeque<Double> detectionTimeStamps = new ArrayDeque<>();
    private final ElapsedTime detectionTimer = new ElapsedTime();

    // Sliding window
    private static final double WINDOW_SEC = 0.5;

    // Detection-rate thresholds
    private static final double MIN_RATE = 6;   // unusable
    private static final double FULL_RATE = 25; // fully trusted

    public AprilTag(HardwareMap hardwareMap) {
        this.telemetry = telemetry;
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTagProcessor)
                .setCameraResolution(new Size(1280, 800))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .build();
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
            lastBearing = targetTagRed.ftcPose.bearing; //degrees
            lastRange = targetTagRed.ftcPose.range;
            detectionRateUpdate(true);
        } else {
            detectionRateUpdate(false);
        }
    }

    public void detectionRateUpdate(boolean targetVisible) {
        double now = detectionTimer.seconds();

        if (targetVisible) {
            detectionTimeStamps.addLast(now);
        }

        // Trim window
        while (!detectionTimeStamps.isEmpty() &&
                detectionTimeStamps.peekFirst() < now - WINDOW_SEC) {
            detectionTimeStamps.removeFirst();
        }
    }

    public double getDetectionRate() {
        return detectionTimeStamps.size() / WINDOW_SEC;
    }

    public double getConfidence() {
        double rate = getDetectionRate();

        double rawConfidence =
                (rate - MIN_RATE) / (FULL_RATE - MIN_RATE);

        // Clamp
        rawConfidence = Range.clip(rawConfidence, 0, 1);

        // Low Pass Filter
        confidence = 0.8 * confidence + 0.2 * rawConfidence;
        return  confidence;
    }

    public double getEffectiveBearing() {
        return lastBearing * getConfidence();
    }
    public double getLastBearing() {
        return lastBearing;
    }
    public double getLastRange() {
        return lastRange;
    }
    public boolean isValid() {
        return getConfidence() > 0.05;
    }

}
