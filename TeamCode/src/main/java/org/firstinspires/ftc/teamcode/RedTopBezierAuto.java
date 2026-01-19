package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "RedTopBezierAuto")
public class RedTopBezierAuto extends LinearOpMode {

    private Follower follower;

    private Timer pathTimer, opModeTimer;
    public enum PathState{
        DRIVE_START_TO_SHOOT,
        DRIVE_SHOOT_TO_INTAKE1,
        DRIVE_SHOOT_TO_INTAKE2,
        DRIVE_SHOOT_TO_INTAKE3,
        DRIVE_INTAKE1_TO_SHOOT,
        DRIVE_INTAKE2_TO_SHOOT,
        DRIVE_INTAKE3_TO_SHOOT,
        SHOOT_1,
        SHOOT_2,
        SHOOT_3,
        SHOOT_4
    }

    PathState pathState;

    private final Pose startPose = new Pose (84, 8, Math.toRadians(0));
    private final Pose shootPose = new Pose (84, 15, Math.toRadians(-20));
    private final Pose intake1Pose = new Pose (126, 35, Math.toRadians(0));
    private final Pose intake2Pose = new Pose (126, 60, Math.toRadians(0));
    private final Pose intake3Pose = new Pose (126, 85, Math.toRadians(0));

    private PathChain driveFromStartToShoot, driveFromShootToIntake1, driveFromIntake1ToShoot, driveFromShootToIntake2, driveFromIntake2ToShoot, driveFromShootToIntake3, driveFromIntake3ToShoot;

    public void buildPaths(){
        //put in coordinates for starting pose to ending pose
        driveFromStartToShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose,  shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        driveFromShootToIntake1 = follower.pathBuilder()
                .addPath(new BezierCurve(shootPose, new Pose(89,44), intake1Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intake1Pose.getHeading())
                .build();

        driveFromIntake1ToShoot = follower.pathBuilder()
                .addPath(new BezierCurve(intake1Pose, new Pose(104, 34), shootPose))
                .setLinearHeadingInterpolation(intake1Pose.getHeading(), shootPose.getHeading())
                .build();

        driveFromShootToIntake2 = follower.pathBuilder()
                .addPath(new BezierCurve(shootPose, new Pose(88,69), intake2Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intake2Pose.getHeading())
                .build();

        driveFromIntake2ToShoot = follower.pathBuilder()
                .addPath(new BezierCurve(intake2Pose, new Pose(101,46), shootPose))
                .setLinearHeadingInterpolation(intake2Pose.getHeading(), shootPose.getHeading())
                .build();

        driveFromShootToIntake3 = follower.pathBuilder()
                .addPath(new BezierCurve(shootPose, new Pose(86,95), intake3Pose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), intake3Pose.getHeading())
                .build();

        driveFromIntake3ToShoot = follower.pathBuilder()
                .addPath(new BezierCurve(intake3Pose, new Pose(100,60), shootPose))
                .setLinearHeadingInterpolation(intake3Pose.getHeading(), shootPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch (pathState){
            case DRIVE_START_TO_SHOOT:
                follower.followPath(driveFromStartToShoot, true);
                setPathState(PathState.SHOOT_1);
                break;
            case SHOOT_1:
                if (!follower.isBusy()){
                    //TODO ADD SHOOTER CODE
                }
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 3) {
                    //TODO STOP SHOOTER
                    setPathState(PathState.DRIVE_SHOOT_TO_INTAKE1);
                }
                break;
            case DRIVE_SHOOT_TO_INTAKE1:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromShootToIntake1, true);
                    setPathState(PathState.DRIVE_INTAKE1_TO_SHOOT);
                }
                break;
            case DRIVE_INTAKE1_TO_SHOOT:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromIntake1ToShoot, true);
                    setPathState(PathState.SHOOT_2);
                }
                break;
            case SHOOT_2:
                if (!follower.isBusy()){
                    //TODO ADD SHOOTER CODE
                }
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 3) {
                    //TODO STOP SHOOTER
                    setPathState(PathState.DRIVE_SHOOT_TO_INTAKE2);
                }
                break;
            case DRIVE_SHOOT_TO_INTAKE2:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromShootToIntake2, true);
                    setPathState(PathState.DRIVE_INTAKE2_TO_SHOOT);
                }
                break;
            case DRIVE_INTAKE2_TO_SHOOT:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromIntake2ToShoot, true);
                    setPathState(PathState.SHOOT_3);
                }
                break;
            case SHOOT_3:
                if (!follower.isBusy()){
                    //TODO ADD SHOOTER CODE
                }
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 3) {
                    //TODO STOP SHOOTER
                    setPathState(PathState.DRIVE_SHOOT_TO_INTAKE3);
                }
                break;
            case DRIVE_SHOOT_TO_INTAKE3:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromShootToIntake3, true);
                    setPathState(PathState.DRIVE_INTAKE3_TO_SHOOT);
                }
                break;
            case DRIVE_INTAKE3_TO_SHOOT:
                if(!follower.isBusy()) {
                    follower.followPath(driveFromIntake3ToShoot, true);
                    setPathState(PathState.SHOOT_4);
                }
                break;
            case SHOOT_4:
                if (!follower.isBusy()){
                    //TODO ADD SHOOTER CODE
                }
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 3) {
                    //TODO STOP SHOOTER
                    telemetry.addLine("Autonomous Finished");
                }
                break;
            default:
                telemetry.addLine("No State Commanded");
                break;
        }
    }

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void runOpMode(){


        pathState = PathState.DRIVE_START_TO_SHOOT;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        //TODO add in any other init mechanisms

        buildPaths();
        follower.setPose(startPose);

        waitForStart();

        opModeTimer.resetTimer();
        setPathState(pathState);

        while(opModeIsActive()){
            follower.update();
            statePathUpdate();

            telemetry.addData("path state", pathState.toString());
            telemetry.addData("x", follower.getPose().getX());
            telemetry.addData("y", follower.getPose().getY());
            telemetry.addData("heading", follower.getPose().getHeading());
            telemetry.addData("Path time", pathTimer.getElapsedTimeSeconds());
        }

    }
}
