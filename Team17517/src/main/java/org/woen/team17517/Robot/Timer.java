package org.woen.team17517.Robot;

public class Timer implements RobotModule{
    double timeForTimer = 0;
    double matchTimeForTimer = 0;
    double currentTime;
    double currentMatchTime;
    UltRobot robot;
    public Timer(UltRobot robot){
        this.robot = robot;
    }
    public void matchStart(double timeTarget){
        currentMatchTime = System.currentTimeMillis();
        this.matchTimeForTimer = timeTarget;
    }
    public void getTimeForTimer(double timeTarget){
        currentTime = System.currentTimeMillis();
        this.timeForTimer = timeTarget;
    }
    @Override
    public boolean isAtPosition() {
        return System.currentTimeMillis()-currentMatchTime>matchTimeForTimer*1000&&
               System.currentTimeMillis()-currentTime > timeForTimer*1000;
    }
    @Override
    public void update() {

    }
}
