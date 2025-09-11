package org.firstinspires.ftc.teamcode.correctors;

import com.arcrobotics.ftclib.controller.PIDController;

public class BalanceCorrector {
    private final PIDController pitchController;
    private final PIDController rollController;

    public BalanceCorrector(double pitchKP, double pitchKI, double pitchKD,
                            double rollKP, double rollKI, double rollKD) {
        this.pitchController = new PIDController(pitchKP, pitchKI, pitchKD);
        this.rollController = new PIDController(rollKP, rollKI, rollKD);
        this.pitchController.setSetPoint(0);
        this.rollController.setSetPoint(0);
        this.pitchController.setTolerance(2.0);
        this.rollController.setTolerance(2.0);
    }

    public BalanceCorrector() {
        this(0.01, 0, 0.001, 0.01, 0, 0.001);
    }
    public double getPitchKP() { return pitchController.getP(); }
    public void setPitchKP(double kp) { pitchController.setP(kp); }

    public double getPitchKI() { return pitchController.getI(); }
    public void setPitchKI(double ki) { pitchController.setI(ki); }

    public double getPitchKD() { return pitchController.getD(); }
    public void setPitchKD(double kd) { pitchController.setD(kd); }
    public double getRollKP() { return rollController.getP(); }
    public void setRollKP(double kp) { rollController.setP(kp); }

    public double getRollKI() { return rollController.getI(); }
    public void setRollKI(double ki) { rollController.setI(ki); }

    public double getRollKD() { return rollController.getD(); }
    public void setRollKD(double kd) { rollController.setD(kd); }
    public double correctForward(double forward, double currentPitchDegrees) {
        if (pitchController.atSetPoint()) {
            return forward;
        }
        double forwardCorrection = pitchController.calculate(currentPitchDegrees);
        return forward + forwardCorrection;
    }

    public double correctStrafe(double strafe, double currentRollDegrees) {
        if (rollController.atSetPoint()) {
            return strafe;
        }
        double strafeCorrection = rollController.calculate(currentRollDegrees);
        return strafe + strafeCorrection;
    }
}
