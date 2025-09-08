package org.firstinspires.ftc.teamcode.loggers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BatteryLogger implements Logger {
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public BatteryLogger(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void addData() {
        double voltage = hardwareMap.voltageSensor.get("Control Hub").getVoltage();
        double percentage = Math.min(100, voltage / 12.0 * 100);
        if (percentage < 20.0) {
            telemetry.addData("Battery Status", "Battery percentage is low... CHARGE IT!!!");
        }
        telemetry.addData("Battery Percentage", "%.1f%% (%.2f V)", percentage, voltage);
    }
}