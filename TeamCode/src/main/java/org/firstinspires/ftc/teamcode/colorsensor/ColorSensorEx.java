package org.firstinspires.ftc.teamcode.colorsensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class ColorSensorEx {

    private final NormalizedColorSensor colorSensor;

    public ColorSensorEx(HardwareMap hardwareMap, String deviceName) {
        this.colorSensor = hardwareMap.get(NormalizedColorSensor.class, deviceName);
    }

    public boolean getColor(ColorSensorColors color) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float red = colors.red / colors.alpha;
        float green = colors.green / colors.alpha;
        float blue = colors.blue / colors.alpha;

        switch (color) {
            case PURPLE:
                return isPurple(red, green, blue);
            case GREEN:
                return isGreen(red, green, blue);
            default:
                return false;
        }
    }

    private boolean isPurple(float red, float green, float blue) {
        return inRange(red, 0.4f, 1.0f) &&
                inRange(blue, 0.3f, 1.0f) &&
                inRange(green, 0f, 0.5f);
    }

    private boolean isGreen(float red, float green, float blue) {
        return inRange(green, 0.5f, 1.0f) &&
                inRange(red, 0f, 0.4f) &&
                inRange(blue, 0f, 0.4f);
    }

    private boolean inRange(float value, float min, float max) {
        return value >= min && value <= max;
    }
}