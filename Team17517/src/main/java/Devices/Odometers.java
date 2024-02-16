package Devices;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Odometers{
    HardwareMap hardwareMap;
    public Odometers(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        odometrX = hardwareMap.get(DcMotorEx.class,"right_back_drive");
        odometrLeftY = hardwareMap.get(DcMotorEx.class,"odometrLeft");
        odometrRightY = hardwareMap.get(DcMotorEx.class,"left_front_drive");
    }
    public DcMotorEx odometrLeftY;
    public DcMotorEx odometrRightY;
    public DcMotorEx odometrX;

}

