package org.woen.team17517.RobotModules.Transport;

import org.woen.team17517.RobotModules.Transport.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Transport.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Transport.Lift.Lift;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class TransportPixels implements RobotModule {
    public Lift lift;
    public GrabberNew grabber;
    UltRobot robot;
    public PixelsCount pixelsCount;
    public TransportPixels(UltRobot robot){
        this.lift = robot.lift;
        this.grabber = robot.grabber;
        this.pixelsCount = robot.pixelsCount;
    }
    public void finishGrabber(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->lift.moveUP(),
                ()->grabber.finish(),
                ()->grabber.open()
        });
    }
    public void safeGrabber(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()-> grabber.close(),
                ()-> grabber.safe()
        });
    }
    public void moveUp(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->grabber.close(),
                ()->grabber.safe(),
                ()->lift.moveUP()
        });
    }
    public void moveDown(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->grabber.open(),
                ()->grabber.safe(),
                ()->lift.moveDown(),
                ()->grabber.down()
        });
    }
    public void stayLift(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->lift.setStopManualTarget()
        });
    }
    public void eatPixels(){
        moveDown();
        if(pixelsCount.isTwoPixelsCount()){
            grabber.brushOut();
        }else grabber.brushIn();
    }
    @Override
    public boolean isAtPosition(){
        return lift.isAtPosition()&&grabber.isAtPosition();
    }

    public void update() {
        lift.update();
        grabber.update();
        pixelsCount.update();
    }
}
