package org.woen.team17517.RobotModules.Intake.Grabber;

import com.qualcomm.robotcore.hardware.AnalogInput;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class PixelsCount implements RobotModule {
    UltRobot robot;
    AnalogInput sensor;
    public PixelsCount(UltRobot robot){
        this.robot = robot;
        sensor =  robot.hardware.sensors.upPixelsSensor;
    }
    private boolean pixelIn;
    public boolean isPixels(int a) {
        return pixelIn && System.currentTimeMillis() - startInTime > a;
    }
    public boolean isFree(int a){
        return !pixelIn && System.currentTimeMillis()-startFreeTime>a;
    }
    private void updatePixelsCount(){pixelIn = sensor.getVoltage() < 0.12;}
    private double startInTime = System.currentTimeMillis();
    private double startFreeTime = System.currentTimeMillis();
    public void update(){
        updatePixelsCount();
        if(!pixelIn) startInTime = System.currentTimeMillis();
        if (pixelIn) startFreeTime = System.currentTimeMillis();
    }
}
