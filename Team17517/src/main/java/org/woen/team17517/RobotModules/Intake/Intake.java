package org.woen.team17517.RobotModules.Intake;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.Intake.Grabber.Brush;
import org.woen.team17517.RobotModules.Intake.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Intake.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Intake.Lift.Lift;
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
    private double startaReversTime = System.currentTimeMillis();
    private double startBrushTime = System.currentTimeMillis();
    public void update(){
        if (isOn) {
            switch (state) {
                case SAVEBRUSH:
                    if(System.currentTimeMillis() - startBrushTime < 1000) brush.out();
                    else                                                   state = State.EATING;
                    break;
                case EATING:
                    grabber.backWallClose();
                    grabber.open();
                    grabber.down();
                    if (robot.hardware.intakeAndLiftMotors.brushMotor.getCurrent(CurrentUnit.AMPS) > 0.9){
                        startBrushTime = System.currentTimeMillis();
                        state = state.SAVEBRUSH;
                    }
                    if(!pixelsCount.isPixels()){
                        brush.in();
                    }else{
                        startaReversTime = System.currentTimeMillis();
                        state = State.REVERSINGAFTEREATING;
                    }
                    break;
                case REVERSINGAFTEREATING:
                    grabber.close();
                    if(System.currentTimeMillis() - startaReversTime < 1500) brush.out();
                    else                                                     state = State.WAITINGDOWN;
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
                    break;
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
                    if (!pixelsCount.isPixels()) state = State.WAITINGDOWN;
                    break;
            }
        }
    }
}
