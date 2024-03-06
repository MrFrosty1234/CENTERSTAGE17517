package org.woen.team17517.RobotModules.EndGame;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.RobotModules.UltRobot;

@Config

public class Plane{
    UltRobot robot;

    public Plane (UltRobot robot) {
        this.robot = robot;
    }

    public static double aimPos = 0.35;
    public static double startPos = 1;
    public static double notStartPose = 0;
    public static double notAimedPos = 0;

    public void aim(){
        robot.hardware.planeServos.aimPlaneServo.setPosition(aimPos);
    }
    public void shoot(){
        robot.hardware.planeServos.startPlaneServo.setPosition(startPos);
    }

}
