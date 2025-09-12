package org.firstinspires.ftc.teamcode.utils;

public final class RangeChecker {
    private RangeChecker() {}
    public static boolean inRange(float value, float minimum, float maximum) {
        return value >= minimum && value <= maximum;
    }
    public static boolean inRange(int value, int minimum, int maximum) {
        return value >= minimum && value <= maximum;
    }
    public static boolean inRange(double value, double minimum, double maximum) {
        return value >= minimum && value <= maximum;
    }
}
