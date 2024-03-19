package org.woen.team17517.Service;

import org.woen.team17517.RobotModules.UltRobot;

public class Timer implements RobotModule {
    double timeForTimer = 0;
    double matchTimeForTimer = 0;
    double startTime = 0;
    double currentMatchTime = 0;
    UltRobot robot;

    public Timer(UltRobot robot) {
        this.robot = robot;
    }

    public void waitSeconds(double timeTarget) {
        startTime = System.currentTimeMillis();
        this.timeForTimer = timeTarget;
    }
    @Override
    public boolean isAtPosition() {
        return System.currentTimeMillis() - startTime > timeForTimer * 1000d;
    }
    @Override
    public void update() {}
}
