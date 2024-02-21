package org.woen.team17517.RobotModules.EndGame;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

@Config

public class Plane implements RobotModule{
    UltRobot robot;

    public Plane (UltRobot robot) {
        this.robot = robot;
    }

    public static double aimPos = 0.35;
    public static double startPos = 0.9;
    public static double notStartPose = 0;
    public static double notAimedPos = 0;

    @Override
    public void update(){
        robot.hardware.planeServos.aimPlaneServo.setPosition(notAimedPos);
        robot.hardware.planeServos.startPlaneServo.setPosition(notStartPose);
    }

    public void planeServoUp(){
        robot.hardware.planeServos.aimPlaneServo.setPosition(aimPos);
    }

    public void planeServoShoot(){
        robot.hardware.planeServos.startPlaneServo.setPosition(startPos);
    }

    public void planeShoot(){
        planeServoUp();
        robot.timer.getTimeForTimer(1);
        planeServoShoot();
    }
}
