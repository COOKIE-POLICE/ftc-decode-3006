package org.firstinspires.ftc.teamcode.manual_integration_tests;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.correctors.BalanceCorrector;
import java.util.Map;
// For PID tuning
@TeleOp(name="Balance Corrector Test", group="Manual Integration Tests")
public class BalanceCorrectorTest extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private MecanumDrive drivetrain;
    private Motor frontLeftMotor;
    private Motor frontRightMotor;
    private Motor backLeftMotor;
    private Motor backRightMotor;
    private BalanceCorrector balanceCorrector;
    private IMU imu;

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
        balanceCorrector = new BalanceCorrector();

        telemetry.addData("Status", "Balance Corrector Test Ready - Robot will try to stay level");
        telemetry.addData("Instructions", "Tilt the robot and watch it try to correct. Tune BalanceCorrector.java default parameters as needed.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double robotPitchDegrees = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
            double robotRollDegrees = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES);

            Map<String, Double> inputs = balanceCorrector.correct(
                    0.0,
                    0.0,
                    0.0,
                    robotPitchDegrees,
                    robotRollDegrees
            );

            double forward = inputs.getOrDefault("forward", 0.0);
            double strafe = inputs.getOrDefault("strafe", 0.0);
            double turn = inputs.getOrDefault("turn", 0.0);

            drivetrain.driveRobotCentric(strafe, forward, turn);

            telemetry.addData("Runtime", runtime.toString());
            telemetry.addData("Pitch", "%.2f degrees", robotPitchDegrees);
            telemetry.addData("Roll", "%.2f degrees", robotRollDegrees);
            telemetry.addData("Forward Correction", "%.3f", forward);
            telemetry.addData("Strafe Correction", "%.3f", strafe);
            telemetry.addData("Status", "Balancing...");
            telemetry.update();
        }
    }
}