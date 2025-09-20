
package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.HardwareMapConfiguration;

public class Constants {
    public static enum LocalizationMethods {
        DRIVE_ENCODER,
        TWO_WHEEL
    }

    public static LocalizationMethods localizationMethod = LocalizationMethods.DRIVE_ENCODER;
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5); // change later
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName(HardwareMapConfiguration.RIGHT_FRONT_MOTOR)
            .rightRearMotorName(HardwareMapConfiguration.RIGHT_BACK_MOTOR)
            .leftRearMotorName(HardwareMapConfiguration.LEFT_BACK_MOTOR)
            .leftFrontMotorName(HardwareMapConfiguration.LEFT_FRONT_MOTOR)
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);
    public static TwoWheelConstants twoWheelConstants = new TwoWheelConstants()
            .forwardEncoder_HardwareMapName("leftFront")
            .strafeEncoder_HardwareMapName("rightRear")
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                    )
            );
    public static DriveEncoderConstants driveEncoderConstants = new DriveEncoderConstants()
            .rightFrontMotorName(HardwareMapConfiguration.RIGHT_FRONT_MOTOR)
            .rightRearMotorName(HardwareMapConfiguration.RIGHT_BACK_MOTOR)
            .leftRearMotorName(HardwareMapConfiguration.LEFT_BACK_MOTOR)
            .leftFrontMotorName(HardwareMapConfiguration.LEFT_FRONT_MOTOR)
            .leftFrontEncoderDirection(Encoder.FORWARD)
            .leftRearEncoderDirection(Encoder.FORWARD)
            .rightFrontEncoderDirection(Encoder.FORWARD)
            .rightRearEncoderDirection(Encoder.FORWARD);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        FollowerBuilder followerBuilder =
                new FollowerBuilder(followerConstants, hardwareMap)
                        .pathConstraints(pathConstraints)
                        .mecanumDrivetrain(driveConstants);
        if (localizationMethod == LocalizationMethods.TWO_WHEEL) {
            followerBuilder.twoWheelLocalizer(twoWheelConstants);
        }
        else if (localizationMethod == LocalizationMethods.DRIVE_ENCODER) {
            followerBuilder.driveEncoderLocalizer(driveEncoderConstants);
        }
        return followerBuilder.build();
    }
}


