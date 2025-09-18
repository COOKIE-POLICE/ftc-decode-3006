package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.constants.BallColor;
public class LEDBallColorIndicator {
    private RevBlinkinLedDriver blinkin;
    public LEDBallColorIndicator(HardwareMap hardwareMap, String blinkinName) {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, blinkinName);
    }

    public void setPurple() {
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
    }

    public void setGreen() {
        blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
    }
    public void setColor(BallColor color) {
        switch (color) {
            case PURPLE:
                setPurple();
                break;
            case GREEN:
                setGreen();
                break;
            case UNKNOWN:
                blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
                break;
        }
    }
}
// docs: https://docs.revrobotics.com/rev-crossover-products/blinkin/duo, look in the onbot java example