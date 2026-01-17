package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "SampleAutoPathing")
public class SampleAutoPathing extends LinearOpMode {

    private Follower follower;

    private Timer pathTimer, opModeTimer;
    public enum PathState{
        //Start position, end position
        //drive - movement state
        //shoot - shooting state
        DRIVE_START_POS_SHOOT_POS,
        SHOOT_PRELOAD
    }

    PathState pathState;

    private final Pose startPose = new Pose (20, 122, Math.toRadians(138));
    private final Pose shootPose = new Pose (46, 96, Math.toRadians(138));

    private PathChain driveFromStartToShoot;

    public void buildPaths(){
        //put in coordinates for starting pose to ending pose
        driveFromStartToShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch (pathState){
            case DRIVE_START_POS_SHOOT_POS:
                follower.followPath(driveFromStartToShoot, true);
                setPathState(PathState.SHOOT_PRELOAD); //rest timer and make new state
                break;
            case SHOOT_PRELOAD:
                //check if follower is done with its path
                if (!follower.isBusy()) {
                    //TODO add logic to flywheel shooter
                    telemetry.addLine("Done Path 1");
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


        pathState = PathState.DRIVE_START_POS_SHOOT_POS;
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