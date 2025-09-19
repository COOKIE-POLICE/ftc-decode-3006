package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.colorsensor.ColorSensorColors;
import org.firstinspires.ftc.teamcode.colorsensor.ColorSensorEx;
import org.firstinspires.ftc.teamcode.constants.BallColor;

public class BallSortMechanism {

    private final ColorSensorEx colorSensor;
    private BallQueue ballQueue;
    private final ElapsedTime unknownColorTimer;
    private static final double UNKNOWN_COLOR_TIMEOUT = 2.0;

    public BallSortMechanism(HardwareMap hardwareMap, BallQueue ballQueue) {
        this.ballQueue = ballQueue;
        this.colorSensor = new ColorSensorEx(hardwareMap, "color_sensor");
        this.unknownColorTimer = new ElapsedTime();
    }

    public void setBallQueue(BallQueue ballQueue) {
        this.ballQueue = ballQueue;
    }

    public void update() {
        if (colorSensor.getColor(ColorSensorColors.GREEN) && ballQueue.getAllowedColor() == BallColor.GREEN) {
            ballQueue.collectFirstBall();
            unknownColorTimer.reset();
        }

        else if (colorSensor.getColor(ColorSensorColors.PURPLE) && ballQueue.getAllowedColor() == BallColor.PURPLE) {
            ballQueue.collectFirstBall();
            unknownColorTimer.reset();
        }

        // Check for known but wrong color
        else {
            spitOutBall();
            unknownColorTimer.reset();
        }
        if (unknownColorTimer.seconds() >= UNKNOWN_COLOR_TIMEOUT) {
            spitOutBall();
            unknownColorTimer.reset();
        }
    }

    private void spitOutBall() {
        // TODO: Add actual implementation for ball ejection
    }
}