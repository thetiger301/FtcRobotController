package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
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
        private Gamepad gamepad1;
        private Drivetrain drivetrain;
        public boolean turningTowardsBlueApriltag = false;
        public boolean turningTowardsRedApriltag = false;

        private boolean blueBearingIsFound = false;
        private boolean redBearingIsFound = false;
        private double bearing = 0;
        public boolean isRedAligned;
        public boolean isBlueAligned;
        public boolean isRedRangeFound = false;
        public boolean isBlueRangeFound = false;
        private double range = 0;
        private double targetRange = 0;
        public boolean isAtTargetRange;
        //public Pattern pattern = null;
        public boolean isPatternDetected;
        public int pattern = -1;

        public AprilTag(HardwareMap hardwareMap, Telemetry telemetry, Drivetrain drivetrain) {
            this.telemetry = telemetry;
            this.drivetrain = drivetrain;
            aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
            visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTagProcessor);
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

            //if the tag information is not found, move 20 degree left
            if (targetTagBlue == null) {
                drivetrain.turnThisManyDegrees(20, .3);
            }
            //once the tag information is found, it will stop scanning
            if (targetTagBlue != null) {
                blueBearingIsFound = true;
                bearing = targetTagBlue.ftcPose.bearing;
            }
            //move as many degrees as the bearing
            if (blueBearingIsFound) {
                if (Math.abs(bearing) > 10) {
                    double targetAngle = bearing;
                    drivetrain.turnThisManyDegrees(targetAngle, .3);
                } else {
                    turningTowardsBlueApriltag = false; //Terminates the method once it has aligned to the tag
                    isBlueAligned = true;
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
            //if the tag information is not found, move 20 degree left
            if(targetTagRed == null){
                drivetrain.turnThisManyDegrees(-20, .3);
            }
            //once the tag information is found, it will stop scanning
            if (targetTagRed != null) {
                redBearingIsFound = true;
                bearing = targetTagRed.ftcPose.bearing;
            }
            //move as many degrees as the bearing
            if (redBearingIsFound){
                if (Math.abs(bearing) > 10) {
                    double targetAngle = bearing;
                    drivetrain.turnThisManyDegrees(targetAngle, .3);
                } else {
                    isRedAligned = true;
                    turningTowardsRedApriltag = false; //Terminates the method once it has aligned to the tag
                }
            }

            //return this variables to original state
            redBearingIsFound = false;
            bearing = 0;
        }

        public void driveTowardsRedApriltag(){
            telemetry.addLine("move to red");
            AprilTagDetection targetTagRed = null;
            //scan for tag information while the bearing is unknown
            if(!isRedRangeFound) {
                List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
                for (AprilTagDetection tag : detections) {
                    if (tag.id == 24) {
                        targetTagRed = tag;
                    }
                }
            }
            //once the tag information is found, it will stop scanning
            if (targetTagRed != null) {
                isRedRangeFound = true;
                range = targetTagRed.ftcPose.range;
            }
            //move forward or backward to target range
            if (isRedRangeFound){
                targetRange = range - 41;
                drivetrain.driveForwardDistance(targetRange, .5);
                isAtTargetRange = true;
            }
            //return this variables to original state
            isRedRangeFound = false;
            range = 0;
        }


        public void driveTowardsBlueApriltag(){
            telemetry.addLine("move to red");
            AprilTagDetection targetTagBlue = null;
            //scan for tag information while the bearing is unknown
            if(!isBlueRangeFound) {
                List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
                for (AprilTagDetection tag : detections) {
                    if (tag.id == 20) {
                        targetTagBlue = tag;
                    }
                }
            }
            //once the tag information is found, it will stop scanning
            if (targetTagBlue != null) {
                isRedRangeFound = true;
                range = targetTagBlue.ftcPose.range;
            }
            //move forward or backward to target range
            if (isBlueRangeFound){
                targetRange = range - 41;
                drivetrain.driveForwardDistance(targetRange, .5);
                isAtTargetRange = true;
            }
            //return this variables to original state
            isBlueRangeFound = false;
            range = 0;
        }
        public void detectPattern() {
            List<AprilTagDetection> detections = aprilTagProcessor.getDetections();
            for (AprilTagDetection tag : detections) {
                if (tag.id == 21) {
                    pattern = 0;
                    isPatternDetected = true;
                } else if (tag.id == 22) {
                    pattern = 1;
                    isPatternDetected = true;
                } else if (tag.id == 23) {
                    pattern = 2;
                    isPatternDetected = true;
                }
            }
            if (!isPatternDetected){
                pattern = 0;
            }
        }
    }
