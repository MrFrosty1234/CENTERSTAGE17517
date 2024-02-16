package Devices;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class GrabberServos{
    HardwareMap hardwareMap;
    public GrabberServos(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        grabberServo = hardwareMap.get(Servo.class,"pixelServoLift");
        upServo = hardwareMap.get(Servo.class,"pixelServoLeft");
        downServo = hardwareMap.get(Servo.class,"pixelServoRight");}
    public Servo upServo;
    public Servo downServo;
    public Servo grabberServo;
}
