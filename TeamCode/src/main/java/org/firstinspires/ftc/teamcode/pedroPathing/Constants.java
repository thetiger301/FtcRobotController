package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("front-left-drive")
            .rightRearMotorName("back-right-drive")
            .leftRearMotorName("back-left-drive")
            .leftFrontMotorName("front-right-drive")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);

    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName("front-right-drive")
            .rightRearMotorName("back-right-drive")
            .leftRearMotorName("back-left-drive")
            .leftFrontMotorName("front-left-drive")
            .leftFrontEncoderDirection(Encoder.FORWARD)
            .leftRearEncoderDirection(Encoder.REVERSE)
            .rightFrontEncoderDirection(Encoder.REVERSE)
            .rightRearEncoderDirection(Encoder.FORWARD)
            .robotLength(13.5)
            .robotWidth(16.5)
            .forwardTicksToInches(0.5)
            .strafeTicksToInches(0.345)
            .turnTicksToInches(-0.056);


    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .driveEncoderLocalizer(localizerConstants)
                /* other builder steps */
                .build();
    }
}
