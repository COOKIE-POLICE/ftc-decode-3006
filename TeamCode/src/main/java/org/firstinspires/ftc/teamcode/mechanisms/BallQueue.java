package org.firstinspires.ftc.teamcode.mechanisms;

import org.firstinspires.ftc.teamcode.constants.BallColor;
import org.firstinspires.ftc.teamcode.constants.MotifPattern;

import java.util.ArrayDeque;
import java.util.Deque;

public class BallQueue {
    private Deque<BallColor> queue;

    public BallQueue(MotifPattern pattern) {
        queue = new ArrayDeque<>();
        initializePattern(pattern);
    }

    public void setPattern(MotifPattern pattern) {
        queue.clear();
        initializePattern(pattern);
    }

    private void initializePattern(MotifPattern pattern) {
        switch (pattern) {
            case GPP:
                queue.addLast(BallColor.GREEN);
                queue.addLast(BallColor.PURPLE);
                queue.addLast(BallColor.PURPLE);
                break;
            case PGP:
                queue.addLast(BallColor.PURPLE);
                queue.addLast(BallColor.GREEN);
                queue.addLast(BallColor.PURPLE);
                break;
            case PPG:
                queue.addLast(BallColor.PURPLE);
                queue.addLast(BallColor.PURPLE);
                queue.addLast(BallColor.GREEN);
                break;
        }
    }

    public BallColor getAllowedColor() {
        if (queue.isEmpty()) {
            return BallColor.UNKNOWN;
        }
        return queue.peekFirst();
    }

    public BallColor collectFirstBall() {
        if (queue.isEmpty()) {
            return BallColor.UNKNOWN;
        }

        BallColor collectedColor = queue.removeFirst();
        queue.addLast(collectedColor);
        return collectedColor;
    }
}