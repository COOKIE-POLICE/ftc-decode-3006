package org.firstinspires.ftc.teamcode.teleops;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "TeleOp3006", group = "TeleOps")
public class TeleOp3006 extends OpMode {
    static final boolean FIELD_CENTRIC_MODE = false;
    static final boolean SQUARED_INPUTS = false;
    private MecanumDrive drivetrain;
    private GamepadEx mainGamepad;
    private IMU imu;

    @Override
    public void init() {
        // <editor-fold desc="Motors">
        drivetrain = new MecanumDrive(
                new Motor(hardwareMap, "leftFrontMotor", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "rightFrontMotor", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "leftBackMotor", Motor.GoBILDA.RPM_312),
                new Motor(hardwareMap, "rightBackMotor", Motor.GoBILDA.RPM_312)
        );
        // </editor-fold>

        // <editor-fold desc="IMU">
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        );
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        // </editor-fold>
        mainGamepad = new GamepadEx(gamepad1);
    }

    @Override
    public void loop() {
        mainGamepad.readButtons();
        double strafe = mainGamepad.getLeftX();
        double forward = mainGamepad.getLeftY();
        double turn = mainGamepad.getRightX();

        if (!FIELD_CENTRIC_MODE) {
            drivetrain.driveRobotCentric(strafe, forward, turn, SQUARED_INPUTS);
        } else {
            double robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            drivetrain.driveFieldCentric(strafe, forward, turn, robotHeading, SQUARED_INPUTS);
        }
        telemetry.update();
    }
}

// https://docs.revrobotics.com/rev-crossover-products/blinkin/duo, look in the onbot java example