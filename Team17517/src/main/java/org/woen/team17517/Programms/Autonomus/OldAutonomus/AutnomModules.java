package org.woen.team17517.Programms.Autonomus.OldAutonomus;

import org.woen.team17517.RobotModules.UltRobot;

public class AutnomModules {
    UltRobot robot;
    public AutnomModules(UltRobot robot){
        this.robot = robot;
    }
    public void Move(double x, double y, double h, double time){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.driveTrainVelocityControl.moveRobotCord(x,y,h),
                ()->robot.timer.getTimeForTimer(time),
                ()->robot.driveTrainVelocityControl.moveRobotCord(0,0,0),
                ()->robot.timer.getTimeForTimer(0.1),

        });
    }
    public void bacBoardPixels(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.close(),
                ()->robot.grabber.safe(),
                ()->robot.timer.getTimeForTimer(0.2),
                ()->robot.lift.moveUP(),
                ()->robot.grabber.finish(),
                ()->robot.grabber.open(),
                ()->robot.timer.getTimeForTimer(1),
                ()->robot.grabber.safe(),
                ()->robot.lift.moveDown(),
                ()->robot.grabber.down()
        });
    }
    public void eatPixels() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.grabber.brushIn(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.close(),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.grabber.brushOut(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.brushOff(),
                () -> robot.timer.getTimeForTimer(0.1),
        });
    }
}
