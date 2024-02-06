package org.woen.team17517.Programms.Autonomus.OldAutonomus;

import org.woen.team17517.RobotModules.UltRobot;

public class AutnomModules {
    UltRobot robot;
    public AutnomModules(UltRobot robot){
        this.robot = robot;
    }
    public void Move(double x, double y, double h, double time){
        robot.updateAllWhileOneRobotModule(robot.timer,new Runnable[]{
                ()->robot.driveTrainVelocityControl.moveRobotCord(x,y,h),
                ()->robot.timer.getTimeForTimer(time),
                ()->robot.driveTrainVelocityControl.moveRobotCord(0,0,0),
                ()->robot.timer.getTimeForTimer(0.1),

        });
    }
    public void bacBoardPixels(){
        robot.updateAllWhileOneRobotModule(robot.transportPixels,new Runnable[]{
                ()-> robot.transportPixels.moveUp(),
                ()-> robot.transportPixels.finishGrabber(),
                ()-> robot.transportPixels.safeGrabber(),
                ()-> robot.transportPixels.moveDown()
        });
    }
    public void eatPixels(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.transportPixels.eatPixels(),
                ()->robot.timer.getTimeForTimer(1),
                ()->robot.transportPixels.grabber.brushOff()
        });
    }
}
