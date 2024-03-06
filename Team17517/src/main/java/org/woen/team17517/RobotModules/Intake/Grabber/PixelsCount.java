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
    public boolean isPixels() {
        return pixelIn && System.currentTimeMillis()-startTime>1200;
    }
    private void updatePixelsCount(){
        pixelIn = sensor.getVoltage() < 0.4;
    }
    private double startTime = System.currentTimeMillis();
    public void update(){
        updatePixelsCount();
        if(!pixelIn) startTime = System.currentTimeMillis();
    }
}
