package org.firstinspires.ftc.teamcode.teleops;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.loggers.ImuLogger;
import org.firstinspires.ftc.teamcode.loggers.BatteryLogger;
import org.firstinspires.ftc.teamcode.loggers.GroupLogger;
import org.firstinspires.ftc.teamcode.correctors.BalanceCorrector;

@TeleOp(name="TeleOp3006", group="TeleOps")
public class TeleOp3006 extends OpMode {
    private MecanumDrive drivetrain;
    private Motor frontLeftMotor;
    private Motor frontRightMotor;
    private Motor backLeftMotor;
    private Motor backRightMotor;
    private GroupLogger groupLogger;
    private BatteryLogger batteryLogger;
    private ImuLogger imuLogger;
    private GamepadEx mainGamepad;
    private BalanceCorrector balanceCorrector;
    private IMU imu;

    static final boolean FIELD_CENTRIC_MODE = false;

    @Override
    public void init() {
        // <editor-fold desc="Motors">
        frontLeftMotor = new Motor(hardwareMap, "frontLeftMotor");
        frontRightMotor = new Motor(hardwareMap, "frontRightMotor");
        backLeftMotor = new Motor(hardwareMap, "backLeftMotor");
        backRightMotor = new Motor(hardwareMap, "backRightMotor");
        drivetrain = new MecanumDrive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
        // </editor-fold>

        // <editor-fold desc="IMU">
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        // </editor-fold>

        // <editor-fold desc="Gamepad and Loggers">
        mainGamepad = new GamepadEx(gamepad1);
        batteryLogger = new BatteryLogger(hardwareMap, telemetry);
        imuLogger = new ImuLogger(imu, telemetry);
        groupLogger = new GroupLogger(batteryLogger, imuLogger);

        balanceCorrector = new BalanceCorrector();
        // </editor-fold>

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        mainGamepad.readButtons();
        double robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double robotPitchDegrees = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
        double robotRollDegrees = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES);
        double strafe = balanceCorrector.correctStrafe(mainGamepad.getLeftX(), robotRollDegrees);
        double forward = balanceCorrector.correctForward(mainGamepad.getLeftY(), robotPitchDegrees);
        double turn = mainGamepad.getRightX();

        if (!FIELD_CENTRIC_MODE) {
            drivetrain.driveRobotCentric(strafe, forward, turn);
        } else {
            drivetrain.driveFieldCentric(strafe, forward, turn, robotHeading);
        }
        telemetry.addData("Status", "Running");
        groupLogger.addData();
        telemetry.update();
    }
}