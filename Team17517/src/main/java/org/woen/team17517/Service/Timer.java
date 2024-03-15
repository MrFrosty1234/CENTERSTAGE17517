package org.woen.team17517.Service;

import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team17517.R;
import org.woen.team17517.RobotModules.UltRobot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class Timer implements RobotModule {
    double timeForTimer = 0;
    double matchTimeForTimer = 0;
    double startTime = 0;
    double currentMatchTime = 0;
    UltRobot robot;

    private double startTimeForRR;
    private final ElapsedTime time = new ElapsedTime();
    private List<BooleanSupplier> wait = new ArrayList<>();

    public Timer(UltRobot robot) {
        this.robot = robot;
    }

    public void matchStart(double timeTarget) {
        currentMatchTime = System.currentTimeMillis();
        this.matchTimeForTimer = timeTarget;
    }

    public void getTimeForTimer(double timeTarget) {
        startTime = System.currentTimeMillis();
        this.timeForTimer = timeTarget;
    }

    public void waitLift(){
       robot.updateWhilePositionFalse(new Runnable[]{
               () -> {
                   wait.add(()-> robot.lift.isAtPosition() && robot.intake.isAtPosition());
                   startTimeForRR = time.seconds();
               }
       });
    }

    @Override
    public boolean isAtPosition() {
        return System.currentTimeMillis() - startTime > timeForTimer * 1000d;
    }

    @Override
    public void update() {
    }
}
