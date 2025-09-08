package org.firstinspires.ftc.teamcode.teleops;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.loggers.BatteryLogger;
import org.firstinspires.ftc.teamcode.loggers.GroupLogger;
import org.firstinspires.ftc.teamcode.correctors.BalanceCorrector;
import java.util.Map;
// TODO: Switch to CommandOpMode later cuz it looks interesting
// TODO: Look into this example later cuz it looks to be useful later on potentially: https://github.com/FTCLib/FTCLib/blob/v2.1.1/examples/src/main/java/com/example/ftclibexamples/PurePursuitSample.java
// TODO: Test out LimeLight
@TeleOp(name="TeleOp3006", group="TeleOps")
public class TeleOp3006 extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumDrive drivetrain;
    private Motor frontLeftMotor;
    private Motor frontRightMotor;
    private Motor backLeftMotor;
    private Motor backRightMotor;
    private GroupLogger groupLogger;
    private BatteryLogger batteryLogger;
    private GamepadEx mainGamepad;
    private BalanceCorrector balanceCorrector;
    private IMU imu;

    static final boolean FIELD_CENTRIC_MODE = false;

    @Override
    public void runOpMode() {
        frontLeftMotor = new Motor(hardwareMap, "frontLeftMotor");
        frontRightMotor = new Motor(hardwareMap, "frontRightMotor");
        backLeftMotor = new Motor(hardwareMap, "backLeftMotor");
        backRightMotor = new Motor(hardwareMap, "backRightMotor");
        drivetrain = new MecanumDrive(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);

        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        mainGamepad = new GamepadEx(gamepad1);
        batteryLogger = new BatteryLogger(hardwareMap, telemetry);
        groupLogger = new GroupLogger(batteryLogger);

        balanceCorrector = new BalanceCorrector();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            mainGamepad.readButtons();

            double robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
            double robotPitchDegrees = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
            double robotRollDegrees = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES);

            Map<String, Double> inputs = balanceCorrector.correct(
                    mainGamepad.getLeftY(),
                    mainGamepad.getLeftX(),
                    mainGamepad.getRightX(),
                    robotPitchDegrees,
                    robotRollDegrees
            );
            // pls tell me how to make android studio stop crying about sum null pointer
            double forward = inputs.getOrDefault("forward", 0.0);
            double strafe = inputs.getOrDefault("strafe", 0.0);
            double turn = inputs.getOrDefault("turn", 0.0);

            if (!FIELD_CENTRIC_MODE) {
                drivetrain.driveRobotCentric(strafe, forward, turn);
            } else {
                drivetrain.driveFieldCentric(strafe, forward, turn, robotHeading);
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            groupLogger.addData();
            telemetry.update();
        }
    }
}