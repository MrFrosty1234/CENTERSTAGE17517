package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PlaneServos{
    HardwareMap hardwareMap;
    public PlaneServos(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        aimPlaneServo = hardwareMap.get(Servo.class,"planeServo");
        startPlaneServo = hardwareMap.get(Servo.class,"planeServoStart");
    }
    public Servo aimPlaneServo;
    public Servo startPlaneServo;

}
