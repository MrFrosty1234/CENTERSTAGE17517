package org.woen.team17517.RobotModules.Intake;


import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.Intake.Grabber.Brush;
import org.woen.team17517.RobotModules.Intake.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Intake.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Intake.Lift.Lift;
import org.woen.team17517.RobotModules.Lighting.Lighting;
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
        light = robot.lighting;
    }
    GrabberNew grabber;
    Lift lift;
    Brush brush;
    PixelsCount pixelsCount;
    Lighting light;
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
    private double startReversTime = System.currentTimeMillis();
    private double startSaveTime = System.currentTimeMillis();
    private double startOffDefenseStart = System.currentTimeMillis();
    public void update(){
        if (isOn) {
            switch (state) {
                case SAVEBRUSH:
                    light.on();
                    if(System.currentTimeMillis() - startSaveTime < 1000) brush.out();
                    else {
                        startOffDefenseStart = System.currentTimeMillis();
                        state = State.EATING;
                    }
                    break;
                case EATING:
                    light.lightMode = Lighting.LightningMode.OFF;
                    if((robot.hardware.intakeAndLiftMotors.brushMotor.getCurrent(CurrentUnit.AMPS) > 4) &&
                            System.currentTimeMillis() - startOffDefenseStart > 1000){
                        startSaveTime = System.currentTimeMillis();
                        state = state.SAVEBRUSH;
                    }
                    if(pixelsCount.isPixels(1000)){
                        robot.linearOpMode.gamepad1.rumble(400);
                        startReversTime = System.currentTimeMillis();
                        state = State.REVERSINGAFTEREATING;
                    }else if (lift.buttonDown.getState()){
                        brush.in();
                        grabber.backWallClose();
                        grabber.open();
                        grabber.down();
                    }
                    break;
                case REVERSINGAFTEREATING:
                    light.on();
                    grabber.close();
                    if(System.currentTimeMillis() - startReversTime < 1700) brush.out();
                    else                                                     state = State.WAITINGDOWN;
                    break;
                case WAITINGDOWN:
                    light.on();
                    lift.moveDown();
                    brush.off();
                    grabber.down();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case WAITINGBACKDROPDOWN:
                    light.on();
                    lift.moveBackDropDown();
                    brush.off();
                    if(lift.getPosition()>200)grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case WAITINBACKDROPCENTER:
                    light.on();
                    lift.moveToMiddle();
                    brush.off();
                    if(lift.getPosition()>200)grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case WAITINGUP:
                    light.on();
                    lift.moveUP();
                    brush.off();
                    if(lift.getPosition()>200)grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                    break;
                case SCORING:
                    if(lift.getPosition()>200) {
                        light.on();
                        grabber.finish();
                        grabber.open();
                        grabber.backWallOpen();
                        if (pixelsCount.isFree(500)) {
                            state = State.WAITINGDOWN;
                        }
                    }else {
                        state = State.WAITINGDOWN;
                    }
                    break;
            }
        }
    }
}
