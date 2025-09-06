package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ControlledMecanumParallelPlateDrivetrain {

    private DcMotor frontLeftDrive;
    private DcMotor backLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backRightDrive;

    private double frontLeftPower;
    private double frontRightPower;
    private double backLeftPower;
    private double backRightPower;

    private Gamepad gamepad1;

    public ControlledMecanumParallelPlateDrivetrain(HardwareMap hardwareMap, String frontLeftName, String backLeftName, String frontRightName, String backRightName, Gamepad gamepad1) {
        this.gamepad1 = gamepad1;

        frontLeftDrive = hardwareMap.get(DcMotor.class, frontLeftName);
        backLeftDrive = hardwareMap.get(DcMotor.class, backLeftName);
        frontRightDrive = hardwareMap.get(DcMotor.class, frontRightName);
        backRightDrive = hardwareMap.get(DcMotor.class, backRightName);

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    public void update() {
        double axial = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        frontLeftPower = axial + lateral + yaw;
        frontRightPower = axial - lateral - yaw;
        backLeftPower = axial - lateral + yaw;
        backRightPower = axial + lateral - yaw;

        normalizePowers();

        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(frontRightPower);
        backLeftDrive.setPower(backLeftPower);
        backRightDrive.setPower(backRightPower);
    }

    private void normalizePowers() {
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }
    }

    public double getFrontLeftPower() { return frontLeftPower; }
    public double getFrontRightPower() { return frontRightPower; }
    public double getBackLeftPower() { return backLeftPower; }
    public double getBackRightPower() { return backRightPower; }
}