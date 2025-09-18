package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.constants.BallColor;

public class BallSortMechanism {

    private final BallColorDetector colorDetector;
    private BallQueue ballQueue;
    private final ElapsedTime unknownColorTimer;
    private boolean isTimerRunning;
    private static final double UNKNOWN_COLOR_TIMEOUT = 2.0;

    public BallSortMechanism(NormalizedColorSensor colorSensor, BallQueue ballQueue) {
        this.ballQueue = ballQueue;
        this.colorDetector = new BallColorDetector(colorSensor);
        this.unknownColorTimer = new ElapsedTime();
        this.isTimerRunning = false;
    }

    public void setBallQueue(BallQueue ballQueue) {
        this.ballQueue = ballQueue;
    }

    public BallColor detectColor() {
        return colorDetector.detectColor();
    }

    public void update() {
        BallColor detectedColor = detectColor();

        if (detectedColor == ballQueue.getAllowedColor()) {
            ballQueue.collectFirstBall();
            resetUnknownColorTimer();

        } else if (detectedColor == BallColor.UNKNOWN) {
            if (!isTimerRunning) {
                startUnknownColorTimer();
            }

            if (unknownColorTimer.seconds() >= UNKNOWN_COLOR_TIMEOUT) {
                spitOutBall();
                resetUnknownColorTimer();
            }

        } else {
            spitOutBall();
            resetUnknownColorTimer();
        }
    }

    private void startUnknownColorTimer() {
        unknownColorTimer.reset();
        isTimerRunning = true;
    }

    private void resetUnknownColorTimer() {
        isTimerRunning = false;
        unknownColorTimer.reset();
    }
    private void spitOutBall() {
        // TODO: Add actual implementation for ball ejection
    }
}