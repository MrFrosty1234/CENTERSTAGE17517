package org.woen.team17517.Programms.TeleOp;

import org.woen.team17517.RobotModules.UltRobot;

public class TeleOpModules {
    UltRobot robot;
    TeleOpModules(UltRobot robot){
        this.robot = robot;
    }


    public void liftUpAndFinishGrabber(){
        robot.updateWhilePositionFalseTimer(5,new Runnable[]{
                ()->robot.grabber.close(),
                ()->robot.grabber.safe(),
                ()->robot.lift.moveUP(),
                ()->robot.grabber.finish(),
        });
    }
    public void liftDownAndOpenGrabber(){
        robot.updateWhilePositionFalseTimer(5,new Runnable[]{
                ()->robot.grabber.safe(),
                ()->robot.lift.moveDown(),
                ()->robot.grabber.down(),
                ()->robot.grabber.open(),
        });
    }
    public void closeGrabber(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.close(),
                ()->robot.grabber.brushOut(),
                ()->robot.timer.getTimeForTimer(1)
        });
    }
    public void openGrabber(){
        robot.grabber.finish();
        robot.grabber.open();
    }
    public void eatPixels(){
        this.liftDownAndOpenGrabber();
        while(!robot.pixelsCount.isTwoPixelsCount()){
            robot.grabber.brushIn();
        }
        robot.grabber.close();
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.brushOut(),
                ()->robot.timer.getTimeForTimer(1)
        });
    }
}
