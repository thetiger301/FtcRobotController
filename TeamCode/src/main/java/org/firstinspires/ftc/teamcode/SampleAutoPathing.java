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


        public void buildPaths() {

            Pose startPose = new Pose(
                    46.5613,
                    109.8891,
                    Math.toRadians(90)
            );

            Pose path1End = new Pose(
                    18.4874,
                    83.1933,
                    Math.toRadians(180)
            );

            Pose path2End = new Pose(
                    56.1479,
                    74.5412,
                    Math.toRadians(59)
            );

            Pose path3End = new Pose(
                    70.4824,
                    14.3832,
                    Math.toRadians(59) // tangential, will be overridden
            );

            driveFromStartToShoot = follower.pathBuilder()

                    // ===== Path 1 =====
                    .addPath(
                            new BezierCurve(
                                    startPose,
                                    new Pose(66.8017, 103.4706),
                                    path1End
                            )
                    )
                    .setLinearHeadingInterpolation(
                            Math.toRadians(144),
                            Math.toRadians(180)
                    )

                    // ===== Path 2 =====
                    .addPath(
                            new BezierCurve(
                                    path1End,
                                    new Pose(27.3479, 65.1025),
                                    path2End
                            )
                    )
                    .setTangentHeadingInterpolation()

                    // ===== Path 3 =====
                    .addPath(
                            new BezierLine(
                                    path2End,
                                    path3End
                            )
                    )
                    .setTangentHeadingInterpolation()

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