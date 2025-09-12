package org.firstinspires.ftc.teamcode.mechanisms.ColorSortMechanism;

import org.firstinspires.ftc.teamcode.constants.Color;
import org.firstinspires.ftc.teamcode.constants.Pattern;

import java.util.ArrayDeque;
import java.util.Deque;

public class BallQueue {
    private Deque<Color> queue;

    public BallQueue(Pattern pattern) {
        queue = new ArrayDeque<>();
        initializePattern(pattern);
    }

    public void setPattern(Pattern pattern) {
        queue.clear();
        initializePattern(pattern);
    }

    private void initializePattern(Pattern pattern) {
        switch (pattern) {
            case GPP:
                queue.addLast(Color.GREEN);
                queue.addLast(Color.PURPLE);
                queue.addLast(Color.PURPLE);
                break;
            case PGP:
                queue.addLast(Color.PURPLE);
                queue.addLast(Color.GREEN);
                queue.addLast(Color.PURPLE);
                break;
            case PPG:
                queue.addLast(Color.PURPLE);
                queue.addLast(Color.PURPLE);
                queue.addLast(Color.GREEN);
                break;
        }
    }

    public Color getAllowedColor() {
        if (queue.isEmpty()) {
            return Color.UNKNOWN;
        }
        return queue.peekFirst();
    }

    public Color collectFirstBall() {
        if (queue.isEmpty()) {
            return Color.UNKNOWN;
        }

        Color collectedColor = queue.removeFirst();
        queue.addLast(collectedColor);
        return collectedColor;
    }
}