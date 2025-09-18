package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.constants.BallColor;
import org.firstinspires.ftc.teamcode.utils.RangeChecker;

public class BallColorDetector {

    private final NormalizedColorSensor colorSensor;
    private static class PurpleThresholds {
        static final float RED_MIN = 0.4f;
        static final float RED_MAX = 1.0f;
        static final float BLUE_MIN = 0.3f;
        static final float BLUE_MAX = 1.0f;
        static final float GREEN_MIN = 0f;
        static final float GREEN_MAX = 0.5f;
    }

    private static class GreenThresholds {
        static final float GREEN_MIN = 0.5f;
        static final float GREEN_MAX = 1.0f;
        static final float RED_MIN = 0f;
        static final float RED_MAX = 0.4f;
        static final float BLUE_MIN = 0f;
        static final float BLUE_MAX = 0.4f;
    }

    public BallColorDetector(NormalizedColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    public BallColor detectColor() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float red = colors.red / colors.alpha;
        float green = colors.green / colors.alpha;
        float blue = colors.blue / colors.alpha;

        if (isPurple(red, green, blue)) {
            return BallColor.PURPLE;
        } else if (isGreen(red, green, blue)) {
            return BallColor.GREEN;
        } else {
            return BallColor.UNKNOWN;
        }
    }

    private boolean isPurple(float red, float green, float blue) {
        return RangeChecker.inRange(red, PurpleThresholds.RED_MIN, PurpleThresholds.RED_MAX) &&
                RangeChecker.inRange(blue, PurpleThresholds.BLUE_MIN, PurpleThresholds.BLUE_MAX) &&
                RangeChecker.inRange(green, PurpleThresholds.GREEN_MIN, PurpleThresholds.GREEN_MAX);
    }

    private boolean isGreen(float red, float green, float blue) {
        return RangeChecker.inRange(green, GreenThresholds.GREEN_MIN, GreenThresholds.GREEN_MAX) &&
                RangeChecker.inRange(red, GreenThresholds.RED_MIN, GreenThresholds.RED_MAX) &&
                RangeChecker.inRange(blue, GreenThresholds.BLUE_MIN, GreenThresholds.BLUE_MAX);
    }
}