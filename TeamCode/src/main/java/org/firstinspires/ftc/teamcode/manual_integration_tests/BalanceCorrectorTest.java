package org.firstinspires.ftc.teamcode.manual_integration_tests;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.correctors.BalanceCorrector;
import org.firstinspires.ftc.teamcode.loggers.BatteryLogger;
import org.firstinspires.ftc.teamcode.loggers.GroupLogger;
import org.firstinspires.ftc.teamcode.loggers.ImuLogger;

/*
 * PID Tuning Controls:
 *
 * A / X               → Increase / Decrease Pitch KP
 * B / Y               → Increase / Decrease Pitch KI
 * Dpad Up / Down      → Increase / Decrease Pitch KD
 * Dpad Left / Right   → Increase / Decrease Roll KP
 * Left Trigger / Right Trigger → Increase / Decrease Roll KI
 * Left Bumper / Right Bumper   → Increase / Decrease Roll KD
 */

@TeleOp(name="Balance Corrector Test", group="Manual Integration Tests")
public class BalanceCorrectorTest extends OpMode {
    private MecanumDrive drivetrain;
    private Motor frontLeftMotor;
    private Motor frontRightMotor;
    private Motor backLeftMotor;
    private Motor backRightMotor;
    private BalanceCorrector balanceCorrector;
    private IMU imu;
    private GroupLogger groupLogger;
    private BatteryLogger batteryLogger;
    private ImuLogger imuLogger;
    private GamepadEx mainGamepad;

    @Override
    public void init() {
        frontLeftMotor = new Motor(hardwareMap, "frontLeftMotor");
        frontRightMotor = new Motor(hardwareMap, "frontRightMotor");
        backLeftMotor = new Motor(hardwareMap, "backLeftMotor");
        backRightMotor = new Motor(hardwareMap, "backRightMotor");

        mainGamepad = new GamepadEx(gamepad1);
        drivetrain = new MecanumDrive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        balanceCorrector = new BalanceCorrector();

        batteryLogger = new BatteryLogger(hardwareMap, telemetry);
        imuLogger = new ImuLogger(imu, telemetry);
        groupLogger = new GroupLogger(batteryLogger, imuLogger);
    }

    @Override
    public void loop() {
        double robotPitchDegrees = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
        double robotRollDegrees = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES);

        double strafe = balanceCorrector.correctStrafe(0, robotRollDegrees);
        double forward = balanceCorrector.correctForward(0, robotPitchDegrees);
        double step = 0.001;

        // <editor-fold desc="PID Tuning">
        if (mainGamepad.getButton(GamepadKeys.Button.A)) {
            balanceCorrector.setPitchKP(balanceCorrector.getPitchKP() + step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.X)) {
            balanceCorrector.setPitchKP(balanceCorrector.getPitchKP() - step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.B)) {
            balanceCorrector.setPitchKI(balanceCorrector.getPitchKI() + step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.Y)) {
            balanceCorrector.setPitchKI(balanceCorrector.getPitchKI() - step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.DPAD_UP)) {
            balanceCorrector.setPitchKD(balanceCorrector.getPitchKD() + step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            balanceCorrector.setPitchKD(balanceCorrector.getPitchKD() - step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            balanceCorrector.setRollKP(balanceCorrector.getRollKP() + step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            balanceCorrector.setRollKP(balanceCorrector.getRollKP() - step);
        }
        if (mainGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5) {
            balanceCorrector.setRollKI(balanceCorrector.getRollKI() + step);
        }
        if (mainGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5) {
            balanceCorrector.setRollKI(balanceCorrector.getRollKI() - step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)) {
            balanceCorrector.setRollKD(balanceCorrector.getRollKD() + step);
        }
        if (mainGamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER)) {
            balanceCorrector.setRollKD(balanceCorrector.getRollKD() - step);
        }
        // </editor-fold>

        drivetrain.driveRobotCentric(strafe, forward, 0);
        // <editor-fold desc="Telemetry">
        telemetry.addData("Pitch KP", balanceCorrector.getPitchKP());
        telemetry.addData("Pitch KI", balanceCorrector.getPitchKI());
        telemetry.addData("Pitch KD", balanceCorrector.getPitchKD());
        telemetry.addData("Roll KP", balanceCorrector.getRollKP());
        telemetry.addData("Roll KI", balanceCorrector.getRollKI());
        telemetry.addData("Roll KD", balanceCorrector.getRollKD());
        groupLogger.addData();
        telemetry.update();
        // </editor-fold>
    }
}
