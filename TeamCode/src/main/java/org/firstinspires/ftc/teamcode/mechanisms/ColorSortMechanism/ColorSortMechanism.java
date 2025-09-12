package org.firstinspires.ftc.teamcode.mechanisms.ColorSortMechanism;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.constants.Color;
import org.firstinspires.ftc.teamcode.utils.RangeChecker;
// TODO: If it remains unknown for more than 2 seconds, spit it out NOW!!!!!
// TODO: If it is purple or green, open the respective paths.
// TODO: Create a separate class for color detection.
public class ColorSortMechanism {

    NormalizedColorSensor colorSensor;
    BallQueue ballQueue;
    public ColorSortMechanism(NormalizedColorSensor colorSensor, BallQueue ballQueue) {
        this.ballQueue = ballQueue;
        this.colorSensor = colorSensor;
    }
    public void setBallQueue(BallQueue ballQueue) {this.ballQueue = ballQueue;}
    public Color detectColor() {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float red = colors.red / colors.alpha;
        float green = colors.green / colors.alpha;
        float blue = colors.blue / colors.alpha;
        // TODO: Tune these thresholds, set gains if needed.
        if (RangeChecker.inRange(red, 0.4f, 1.0f) &&
                RangeChecker.inRange(blue, 0.3f, 1.0f) &&
                RangeChecker.inRange(green, 0f, 0.5f)) {
            return Color.PURPLE;
        } else if (RangeChecker.inRange(green, 0.5f, 1.0f) &&
                RangeChecker.inRange(red, 0f, 0.4f) &&
                RangeChecker.inRange(blue, 0f, 0.4f)) {
            return Color.GREEN;
        } else {
            return Color.UNKNOWN;
        }
    }
    public void update() {
        if (detectColor() == ballQueue.getAllowedColor()) {
            ballQueue.collectFirstBall();
        }
        else if (detectColor() == Color.UNKNOWN) {
            // start timer for 2 seconds if not started already. If time up, spit out ball
        }
        else {
            // spit out ball
        }

    }
}
