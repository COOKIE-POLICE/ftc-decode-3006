package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.subsystems.ControlledMecanumParallelPlateDrivetrain;
import org.firstinspires.ftc.teamcode.loggers.BatteryLogger;
@TeleOp(name="TeleOp3006", group="TeleOp")
public class TeleOp3006 extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private ControlledMecanumParallelPlateDrivetrain drivetrain;
    private BatteryLogger batteryLogger;

    @Override
    public void runOpMode() {
        drivetrain = new ControlledMecanumParallelPlateDrivetrain(hardwareMap,
                "front_left_drive", "back_left_drive", "front_right_drive", "back_right_drive", gamepad1);
        batteryLogger = new BatteryLogger(hardwareMap, telemetry);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            drivetrain.update();

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f",
                    drivetrain.getFrontLeftPower(), drivetrain.getFrontRightPower());
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f",
                    drivetrain.getBackLeftPower(), drivetrain.getBackRightPower());
            batteryLogger.update();
            telemetry.update();
        }
    }
}