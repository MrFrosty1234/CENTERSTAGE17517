package org.woen.team17517.RobotModules.EndGame;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.RobotModules.UltRobot;

@Config

public class Plane{
    UltRobot robot;

    public Plane (UltRobot robot) {
        this.robot = robot;
    }

    public static double upPos = 0.35;
    public static double shootPos = 0.5;
    public static double downPos = 0.1;
    public enum PlaneStatus{
        SHOOT,UP,DOWN
    }
    private PlaneStatus status = PlaneStatus.DOWN;

    public PlaneStatus getStatus() {
        return status;
    }

    public void up(){
        robot.hardware.planeServos.aimPlaneServo.setPosition(upPos);
        status = PlaneStatus.UP;
    }
    public void shoot(){
        robot.hardware.planeServos.startPlaneServo.setPosition(shootPos);
        status = PlaneStatus.SHOOT;
    }
    public void down(){
        robot.hardware.planeServos.aimPlaneServo.setPosition(downPos);
        status = PlaneStatus.DOWN;
    }
}
