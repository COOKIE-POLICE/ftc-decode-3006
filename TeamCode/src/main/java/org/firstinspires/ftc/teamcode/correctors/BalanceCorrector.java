package org.firstinspires.ftc.teamcode.correctors;

import com.arcrobotics.ftclib.controller.PIDFController;
import java.util.HashMap;
import java.util.Map;

// Docs for PIDFController: https://docs.ftclib.org/ftclib/features/controllers
/*
 * PID TUNING GUIDE:
 * 1. Start: kP=0.01, kI=0, kD=0.001, kF=0
 * 2. Adjust kP: Too weak = increase, Too jerky = decrease
 * 3. Add kD if oscillating, Add kI only if never reaches level
 * 4. Oscillating = reduce kP, Slow = increase kP
 */
// Didn't tune yet
public class BalanceCorrector {
    private final PIDFController pitchController;
    private final PIDFController rollController;

    public BalanceCorrector(
            double kP, double kI, double kD, double kF) {
        // pitch and roll gunna use the same gains, hope this doesn't come and bite my booty cheeks
        this.pitchController = new PIDFController(kP, kI, kD, kF);
        this.rollController = new PIDFController(kP, kI, kD, kF);
        this.pitchController.setSetPoint(0);
        this.rollController.setSetPoint(0);
        this.pitchController.setTolerance(2.0);
        this.rollController.setTolerance(2.0);
    }

    public BalanceCorrector() {
        this(
                0.01, 0, 0.001, 0
        );
    }

    public Map<String, Double> correct(double forward, double strafe, double turn,
                                       double currentPitchDegrees, double currentRollDegrees) {
        Map<String, Double> inputs = new HashMap<>();

        if (pitchController.atSetPoint() && rollController.atSetPoint()) {
            inputs.put("forward", forward);
            inputs.put("strafe", strafe);
            inputs.put("turn", turn);
            return inputs;
        }
        double forwardCorrection = pitchController.calculate(currentPitchDegrees);
        double strafeCorrection = rollController.calculate(currentRollDegrees);
        inputs.put("forward", forward + forwardCorrection);
        inputs.put("strafe", strafe + strafeCorrection);
        inputs.put("turn", turn); // turn is useless btw, just added this cuz it looks unclean af without it
        return inputs;
    }
}