package org.firstinspires.ftc.teamcode.loggers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupLogger implements Logger {
    private List<Logger> loggers;

    public GroupLogger(Logger... loggers) {
        this.loggers = new ArrayList<>(Arrays.asList(loggers));
    }

    public void addData() {
        for (Logger logger : loggers) {
            logger.addData();
        }
    }
}