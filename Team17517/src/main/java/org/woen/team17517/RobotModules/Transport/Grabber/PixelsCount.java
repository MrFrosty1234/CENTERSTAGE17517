package org.woen.team17517.RobotModules.Transport.Grabber;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class PixelsCount implements RobotModule {
    UltRobot robot;
    AnalogInput upSensor;
    AnalogInput downSensor;
    public PixelsCount(UltRobot robot){
        this.robot = robot;
        upSensor =  robot.devices.upPixelsSensor;
        downSensor = robot.devices.downPixelSensor;
    }
    private double downVolt = 0;
    private double upVolt = 0;

    public double getDownVolt() {
        return downVolt;
    }

    public double getUpVolt() {
        return upVolt;
    }

    private boolean down;
    private boolean up;
    public boolean isTwoPixelsCount() {
        return down&&up;
    }
    private void  updatePixelsCount(){
        down = downSensor.getVoltage() < 3;
        up = upSensor.getVoltage() < 3;
    }
    public void update(){
        upVolt = upSensor.getVoltage();
        downVolt = downSensor.getVoltage();
        updatePixelsCount();
    }
}
