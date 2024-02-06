package org.woen.team17517.RobotModules.Transport;

import org.woen.team17517.RobotModules.Transport.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Transport.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Transport.Lift.Lift;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class TransportPixels implements RobotModule {
    public Lift lift;
    public GrabberNew grabber;
    public PixelsCount pixelsCount;
    public TransportPixels(Lift lift, GrabberNew grabber, PixelsCount pixelsCount){
        this.lift = lift;
        this.grabber = grabber;
        this.pixelsCount = pixelsCount;
    }
    public void finishGrabber(){
        UltRobot.updateOneWhileOneRobotModule(this,new Runnable[]{
                ()->lift.moveUP(),
                ()->grabber.finish(),
                ()->grabber.open()
        });
    }
    public void safeGrabber(){
        UltRobot.updateOneWhileOneRobotModule(grabber,new Runnable[]{
                ()-> grabber.close(),
                ()-> grabber.safe()
        });
    }
    public void moveUp(){
        UltRobot.updateOneWhileOneRobotModule(this, new Runnable[]{
                ()->grabber.close(),
                ()->grabber.safe(),
                ()->lift.moveUP()
        });
    }
    public void moveDown(){
        UltRobot.updateOneWhileOneRobotModule(this,new Runnable[]{
                ()->grabber.open(),
                ()->grabber.safe(),
                ()->lift.moveDown(),
                ()->grabber.down()
        });
    }
    public void stayLift(){
        UltRobot.updateOneWhileOneRobotModule(this,new Runnable[]{
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
