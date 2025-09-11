package org.firstinspires.ftc.teamcode.loggers;

import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class ImuLogger implements Logger {
    private final IMU imu;
    private final Telemetry telemetry;

    public ImuLogger(IMU imu, Telemetry telemetry) {
        this.imu = imu;
        this.telemetry = telemetry;
    }

    @Override
    public void addData() {
        double yaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        double pitch = imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
        double roll = imu.getRobotYawPitchRollAngles().getRoll(AngleUnit.DEGREES);

        telemetry.addData("IMU Yaw", "%.2f°", yaw);
        telemetry.addData("IMU Pitch", "%.2f°", pitch);
        telemetry.addData("IMU Roll", "%.2f°", roll);
    }
}
