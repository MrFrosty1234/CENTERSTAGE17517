    package org.woen.team17517.RobotModules.Intake.Grabber;

import com.qualcomm.robotcore.hardware.AnalogInput;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.RobotModule;

public class OpticalSensor implements RobotModule {
    UltRobot robot;
    AnalogInput sensor;
    public OpticalSensor(UltRobot robot){
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
    Button but = new Button();
    public boolean isFreeOne(){
        return but.update(!pixelIn);
    }
    private void updatePixelsCount(){pixelIn = sensor.getVoltage() < 0.12;}
    private double startInTime = System.currentTimeMillis();
    public double startFreeTime = System.currentTimeMillis();
    public void update(){
        updatePixelsCount();
        if(!pixelIn) startInTime = System.currentTimeMillis();
        if (pixelIn) startFreeTime = System.currentTimeMillis();
    }
}
