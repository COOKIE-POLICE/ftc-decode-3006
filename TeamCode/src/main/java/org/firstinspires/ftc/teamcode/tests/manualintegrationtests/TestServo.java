package org.firstinspires.ftc.teamcode.tests.manualintegrationtests;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Test servo teleop cuz don't got servo tester
@TeleOp(name = "Test Servo", group = "Manual Integration Tests")
public class TestServo extends OpMode {

    private final List<String> servoNames = new ArrayList<>();
    private final List<ServoEx> servos = new ArrayList<>();
    private final double minAngle = 0.0;
    private final double maxAngle = 180.0;
    private final double joystickSensitivity = 0.5;
    private int currentIndex = 0;

    private boolean lastDpadLeft = false;
    private boolean lastDpadRight = false;
    private boolean lastA = false, lastB = false, lastX = false, lastY = false;

    private double currentAngle = 90.0;

    @Override
    public void init() {
        for (Map.Entry<String, Servo> entry : hardwareMap.servo.entrySet()) {
            String name = entry.getKey();
            servoNames.add(name);
            servos.add(new SimpleServo(hardwareMap, name, minAngle, maxAngle));
        }
    }

    @Override
    public void loop() {
        if (servos.isEmpty()) {
            telemetry.addLine("No servos detected");
            telemetry.update();
            return;
        }

        if (gamepad1.dpad_left && !lastDpadLeft) {
            currentIndex = (currentIndex - 1 + servos.size()) % servos.size();
        }
        if (gamepad1.dpad_right && !lastDpadRight) {
            currentIndex = (currentIndex + 1) % servos.size();
        }
        lastDpadLeft = gamepad1.dpad_left;
        lastDpadRight = gamepad1.dpad_right;

        if (gamepad1.a && !lastA) currentAngle = minAngle;
        if (gamepad1.b && !lastB) currentAngle = 90.0;
        if (gamepad1.y && !lastY) currentAngle = maxAngle;
        if (gamepad1.x && !lastX) currentAngle = 45.0;

        lastA = gamepad1.a;
        lastB = gamepad1.b;
        lastX = gamepad1.x;
        lastY = gamepad1.y;

        double delta = gamepad1.left_stick_x * joystickSensitivity;
        currentAngle += delta;

        ServoEx active = servos.get(currentIndex);
        active.turnToAngle(currentAngle);

        telemetry.addLine("=== Servo Test Instructions ===");
        telemetry.addLine("D-pad Left/Right: Cycle through servos");
        telemetry.addLine("A=0°, B=90°, X=45°, Y=180°: Set preset angles");
        telemetry.addLine("Left Stick X: Adjust servo angle gradually");
        telemetry.addData("Active Servo", servoNames.get(currentIndex));
        telemetry.addData("Servo Index", currentIndex + 1 + " of " + servos.size());
        telemetry.addData("Current Angle (deg)", "%.1f", currentAngle);
        telemetry.update();
    }
}
