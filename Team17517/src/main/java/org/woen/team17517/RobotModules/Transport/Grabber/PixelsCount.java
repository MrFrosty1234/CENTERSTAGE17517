package org.woen.team17517.RobotModules.Transport.Grabber;

import com.qualcomm.robotcore.hardware.AnalogSensor;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class PixelsCount implements RobotModule {
    UltRobot robot;
    AnalogSensor upSensor;
    AnalogSensor downSensor;
    public PixelsCount(UltRobot robot){
        this.robot = robot;
        upSensor =  robot.devices.upPixelsSensor;
        downSensor = robot.devices.downPixelSensor;
    }
    boolean down;
    boolean up;
    public boolean isTwoPixelsCount() {
        return down&&up;
    }
    private void  updatePixelsCount(){
        down = downSensor.readRawVoltage()>0;
        up = upSensor.readRawVoltage()>0;
    }
    public void update(){
        updatePixelsCount();
    }
}
