package Devices;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lights {
    HardwareMap hardwareMap;
    public Lights(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        light1 = hardwareMap.get(DcMotorEx.class,"lightning");
        light2 = hardwareMap.get(DcMotorEx.class,"odometrLeft");
    }
    public DcMotorEx light1;
    public DcMotorEx light2;
}
