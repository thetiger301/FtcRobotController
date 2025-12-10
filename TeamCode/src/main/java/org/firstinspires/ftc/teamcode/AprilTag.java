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
        public boolean turningTowardsBlueApriltag = false;
        public boolean turningTowardsRedApriltag = false;

        private boolean blueBearingIsFound = false;
        private boolean redBearingIsFound = false;
        private double bearing = 0;

        public AprilTag(HardwareMap hardwareMap, Telemetry telemetry) {
            this.telemetry = telemetry;
            aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
            visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTagProcessor);
            drivetrain = new Drivetrain(hardwareMap, telemetry);
        }
        public void giveBearing() {
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
                double bearing = targetTagRed.ftcPose.bearing;  // degrees
                telemetry.addData("Target Tag", "Red Tag");
                telemetry.addData("Bearing (deg)", "%.1f", bearing);
            } else if (targetTagBlue != null) {
                double bearing = targetTagBlue.ftcPose.bearing;  // degrees
                telemetry.addData("Target Tag", "Blue Tag");
                telemetry.addData("Bearing (deg)", "%.1f", bearing);
            } else {
                telemetry.addLine("Tag not Detected");
            }
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

        public void faceBlueAprilTag() {
            telemetry.addLine("move to blue");
            AprilTagDetection targetTagBlue = null;
            //scan for tag information while the bearing is unknown
            if (!blueBearingIsFound) {
                List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
                for (AprilTagDetection tag : detections) {
                    if (tag.id == 20) {
                        targetTagBlue = tag;
                    }
                }
            }

            //if the tag information is not found, move 30 degree left
            if (targetTagBlue == null) {
                double targetAngle = 20 + drivetrain.getHeading();
                drivetrain.turnThisManyDegrees(targetAngle, .5);
            }
            //once the tag information is found, it will stop scanning
            if (targetTagBlue != null) {
                blueBearingIsFound = true;
                bearing = targetTagBlue.ftcPose.bearing;
            }
            //move as many degrees as the bearing
            if (blueBearingIsFound) {
                if (Math.abs(bearing) > 2) {
                    double targetAngle = bearing + drivetrain.getHeading();
                    drivetrain.turnThisManyDegrees(targetAngle, .5);
                } else {
                    turningTowardsBlueApriltag = false; //Terminates the method once it has aligned to the tag
                }
            }
            //return all variables to original state
            blueBearingIsFound = false;
            bearing = 0;
        }

        public void faceRedAprilTag(){
            telemetry.addLine("move to red");
            AprilTagDetection targetTagRed = null;
            //scan for tag information while the bearing is unknown
            if(!redBearingIsFound) {
                List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
                for (AprilTagDetection tag : detections) {
                    if (tag.id == 24) {
                        targetTagRed = tag;
                    }
                }
            }
            //if the tag information is not found, move 30 degree left
            if(targetTagRed == null){
                double targetAngle = drivetrain.getHeading() - 20;
                drivetrain.turnThisManyDegrees(targetAngle, .5);
            }
            //once the tag information is found, it will stop scanning
            if (targetTagRed != null) {
                redBearingIsFound = true;
                bearing = targetTagRed.ftcPose.bearing;
            }
            //move as many degrees as the bearing
            if (redBearingIsFound){
                if (Math.abs(bearing) > 2) {
                    double targetAngle =bearing + drivetrain.getHeading();
                    drivetrain.turnThisManyDegrees(targetAngle, .5);
                } else {
                    turningTowardsRedApriltag = false; //Terminates the method once it has aligned to the tag
                }
            }
            //return this variables to original state
            redBearingIsFound = false;
            bearing = 0;
        }
    }
