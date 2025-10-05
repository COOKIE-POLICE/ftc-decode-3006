package org.firstinspires.ftc.teamcode.tests.manualintegrationtests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TeleOp(name = "Test Motor", group = "Manual Integration Tests")
public class TestMotor extends OpMode {

    public static final double POWER_SENSITIVITY = 0.02;
    public static final double VELOCITY_SENSITIVITY = 10.0;
    public static final int POSITION_SENSITIVITY = 100;
    public static final double RUN_TO_POSITION_POWER = 0.5;
    public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;

    private static final DcMotor.RunMode[] RUN_MODES = {
            DcMotor.RunMode.RUN_USING_ENCODER,
            DcMotor.RunMode.RUN_WITHOUT_ENCODER,
            DcMotor.RunMode.RUN_TO_POSITION
    };

    private final List<String> motorNames = new ArrayList<>();
    private final List<DcMotorEx> motors = new ArrayList<>();

    private int currentMotorIndex = 0;
    private int currentModeIndex = 0;
    private double currentPower = 0.0;
    private double currentVelocity = 0.0;
    private int targetPosition = 0;

    private boolean lastDpadLeft = false;
    private boolean lastDpadRight = false;
    private boolean lastDpadUp = false;
    private boolean lastDpadDown = false;
    private boolean lastA = false;

    @Override
    public void init() {
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            String name = entry.getKey();
            motorNames.add(name);
            DcMotorEx motor = hardwareMap.get(DcMotorEx.class, name);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(RUN_MODES[0]);
            motor.setZeroPowerBehavior(ZERO_POWER_BEHAVIOR);
            motors.add(motor);
        }
    }

    @Override
    public void loop() {
        if (motors.isEmpty()) {
            telemetry.addLine("No motors detected");
            telemetry.update();
            return;
        }

        handleMotorSelection();
        DcMotorEx activeMotor = motors.get(currentMotorIndex);
        handleModeChanges(activeMotor);
        handleMotorControl(activeMotor);
        displayTelemetry(activeMotor);
    }

    private void handleMotorSelection() {
        if (gamepad1.dpad_left && !lastDpadLeft) {
            currentMotorIndex = (currentMotorIndex - 1 + motors.size()) % motors.size();
        }
        if (gamepad1.dpad_right && !lastDpadRight) {
            currentMotorIndex = (currentMotorIndex + 1) % motors.size();
        }
        lastDpadLeft = gamepad1.dpad_left;
        lastDpadRight = gamepad1.dpad_right;
    }

    private void handleModeChanges(DcMotorEx activeMotor) {
        if (gamepad1.dpad_up && !lastDpadUp) {
            currentModeIndex = (currentModeIndex + 1) % RUN_MODES.length;
            activeMotor.setMode(RUN_MODES[currentModeIndex]);
        }
        if (gamepad1.dpad_down && !lastDpadDown) {
            currentModeIndex = (currentModeIndex - 1 + RUN_MODES.length) % RUN_MODES.length;
            activeMotor.setMode(RUN_MODES[currentModeIndex]);
        }
        lastDpadUp = gamepad1.dpad_up;
        lastDpadDown = gamepad1.dpad_down;

        if (gamepad1.a && !lastA) {
            currentPower = 0;
            currentVelocity = 0;
            targetPosition = 0;
            activeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            activeMotor.setMode(RUN_MODES[currentModeIndex]);
        }

        lastA = gamepad1.a;
    }

    private void handleMotorControl(DcMotorEx activeMotor) {
        if (activeMotor.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            int delta = (int) (gamepad1.left_stick_y * -POSITION_SENSITIVITY);
            targetPosition += delta;
            activeMotor.setTargetPosition(targetPosition);
            activeMotor.setPower(RUN_TO_POSITION_POWER);
        } else if (activeMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
            double delta = gamepad1.left_stick_y * -VELOCITY_SENSITIVITY;
            currentVelocity += delta;
            activeMotor.setVelocity(currentVelocity);
        } else {
            double delta = gamepad1.left_stick_y * -POWER_SENSITIVITY;
            currentPower += delta;
            activeMotor.setPower(currentPower);
        }
    }

    private void displayTelemetry(DcMotorEx activeMotor) {
        telemetry.addLine("=== Motor Test Instructions ===");
        telemetry.addLine("D-pad Left/Right: Cycle through motors");
        telemetry.addLine("D-pad Up/Down: Cycle through run modes");
        telemetry.addLine("A: Reset");
        telemetry.addLine("Left Stick Y: Adjust velocity/power/position");
        telemetry.addData("Active Motor", motorNames.get(currentMotorIndex));
        telemetry.addData("Motor Index", currentMotorIndex + 1 + " of " + motors.size());
        telemetry.addData("Current Position", activeMotor.getCurrentPosition());
        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Current Velocity (ticks/sec)", "%.2f", currentVelocity);
        telemetry.addData("Actual Velocity (ticks/sec)", "%.2f", activeMotor.getVelocity());
        telemetry.addData("Current Power", "%.2f", currentPower);
        telemetry.addData("Run Mode", activeMotor.getMode());
        telemetry.update();
    }
}