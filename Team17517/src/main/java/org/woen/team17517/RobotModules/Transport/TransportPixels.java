package org.woen.team17517.RobotModules.Transport;

import org.checkerframework.checker.units.qual.A;
import org.woen.team17517.RobotModules.Transport.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Transport.Lift.Lift;
import org.woen.team17517.RobotModules.Transport.Lift.LiftMode;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class TransportPixels implements RobotModule {
    public Lift lift;
    public GrabberNew grabber;
    public TransportPixels(Lift lift, GrabberNew grabber){
        this.lift = lift;
        this.grabber = grabber;
    }
    public void finishGrabber(){
        UltRobot.updateWhile(lift,new Runnable[]{
                ()->lift.moveUP(),
                ()->grabber.finish(),
                ()->grabber.open()
        });
    }
    public void safeGrabber(){
        UltRobot.updateWhile(grabber,new Runnable[]{
                ()-> grabber.close(),
                ()-> grabber.safe()
        });
    }
    public void moveUp(){
        UltRobot.updateWhile(lift, new Runnable[]{
                ()->grabber.close(),
                ()->grabber.safe(),
                ()->lift.moveUP()
        });
    }
    public void moveDown(){
        UltRobot.updateWhile(lift,new Runnable[]{
                ()->grabber.open(),
                ()->grabber.safe(),
                ()->lift.moveDown(),
                ()->grabber.down()
        });
    }
    public void stayLift(){
        UltRobot.updateWhile(lift,new Runnable[]{
                ()->lift.setStopManualTarget()
        });
    }
    @Override
    public boolean isAtPosition(){
        return lift.isAtPosition()&&grabber.isAtPosition();
    }

    public void update() {
        lift.update();
        grabber.update();
    }
}
