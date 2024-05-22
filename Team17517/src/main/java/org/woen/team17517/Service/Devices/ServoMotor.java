package org.woen.team17517.Service.Devices;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoMotor extends Device<Servo>{
    public ServoMotor(String name){
        __init(name,Servo.class);
    }
    public void setValue(double target){
        device.setPosition(target);
    }
}
