package org.woen.team17517.RobotModules.Grabber;

import com.qualcomm.robotcore.hardware.AnalogInput;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.RobotModule;

public class PixelsCount implements RobotModule {
    UltRobot robot;
    AnalogInput upSensor;
    AnalogInput downSensor;
    public PixelsCount(UltRobot robot){
        this.robot = robot;
        upSensor =  robot.hardware.sensors.upPixelsSensor;
    }
    private double upVolt = 0;

    public double getUpVolt() {
        return upVolt;
    }
    private boolean up;
    public boolean isPixels() {
        return up;
    }
    private void  updatePixelsCount(){
        up = upSensor.getVoltage() < 0.4;
    }
    Button but = new Button();
    double startTime = System.currentTimeMillis();
    public void update(){
        upVolt = upSensor.getVoltage();
        updatePixelsCount();
    }
}
