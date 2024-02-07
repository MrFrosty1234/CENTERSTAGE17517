package org.woen.team17517.RobotModules.Transport;

import org.woen.team17517.RobotModules.Transport.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Transport.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Transport.Lift.Lift;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class TransportPixels {
    UltRobot robot;
    public PixelsCount pixelsCount;
    public TransportPixels(UltRobot robot){
        this.pixelsCount = robot.pixelsCount;
    }
    public void finishGrabber(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.lift.moveUP(),
                ()->robot.grabber.finish(),
                ()->robot.grabber.open()
        });
    }
    public void safeGrabber(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()-> robot.grabber.close(),
                ()-> robot.grabber.safe()
        });
    }
    public void moveUp(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.close(),
                ()->robot.grabber.safe(),
                ()->robot.lift.moveUP()
        });
    }
    public void moveDown(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.open(),
                ()->robot.grabber.safe(),
                ()->robot.lift.moveDown(),
                ()->robot.grabber.down()
        });
    }
    public void stayLift(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.lift.setStopManualTarget()
        });
    }
    public void eatPixels(){
        moveDown();
        if(robot.pixelsCount.isTwoPixelsCount()){
            robot.grabber.brushOut();
        }else robot.grabber.brushIn();
    }
}
