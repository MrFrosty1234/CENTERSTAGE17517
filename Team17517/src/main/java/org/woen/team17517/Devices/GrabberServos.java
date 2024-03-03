package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberServos{
    HardwareMap hardwareMap;
    public GrabberServos(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        grabberServo = hardwareMap.get(Servo.class,"pixelServoLift");
        upServo = hardwareMap.get(Servo.class,"pixelServoLeft");
        backWallServo = hardwareMap.get(Servo.class,"pixelServoRight");
        autonomServo = hardwareMap.get(Servo.class, "autonomServo");
    }
    public Servo upServo;
    public Servo backWallServo;
    public Servo grabberServo;
    public Servo autonomServo;
}
