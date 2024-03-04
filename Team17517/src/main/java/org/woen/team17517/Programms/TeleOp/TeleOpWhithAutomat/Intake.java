package org.woen.team17517.Programms.TeleOp.TeleOpWhithAutomat;

import org.woen.team17517.RobotModules.Grabber.Brush;
import org.woen.team17517.RobotModules.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Lift.Lift;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Intake implements RobotModule {
    UltRobot robot;

    public Intake(UltRobot robot) {
        this.robot = robot;
        grabber = robot.grabber;
        lift = robot.lift;
        brush = robot.brush;
        pixelsCount = robot.pixelsCount;
    }
    GrabberNew grabber;
    Lift lift;
    Brush brush;
    PixelsCount pixelsCount;
    private boolean isOn = false;
    public void on(){
        isOn = true;
    }
    public void off(){
        isOn = false;
    }
    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    State state = State.WAITINGDOWN;
    private double startTime = System.currentTimeMillis();
    public void update(){
        if (isOn) {
            switch (state) {
                case EATING:
                    grabber.backWallClose();
                    grabber.open();
                    grabber.down();
                    if(!pixelsCount.isPixels()){
                        brush.in();
                    }else{
                        startTime = System.currentTimeMillis();
                        state = State.REVERSINGAFTEREATING;
                    }
                    break;
                case REVERSINGAFTEREATING:
                    if(System.currentTimeMillis() - startTime<1000) {
                        grabber.close();
                    }
                    if(System.currentTimeMillis() - startTime < 1500)
                        brush.out();
                    else {
                        startTime = System.currentTimeMillis();
                        brush.off();
                        state = State.WAITINGDOWN;
                    }
                    break;
                case WAITINGDOWN:
                    lift.moveDown();
                    brush.off();
                    grabber.down();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case WAITINGBACKDROPDOWN:
                    lift.moveBackDropDown();
                    grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                case WAITINGUP:
                    lift.moveUP();
                    grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case SCORING:
                    grabber.finish();
                    grabber.open();
                    grabber.backWallOpen();
                    if (!pixelsCount.isPixels()){
                        state = State.WAITINGDOWN;
                    }
                    break;

            }
        }
    }
}
