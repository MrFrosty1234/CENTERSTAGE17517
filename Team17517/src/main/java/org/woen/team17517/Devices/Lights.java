package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lights {
    HardwareMap hardwareMap;
    public Lights(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        light1 = hardwareMap.get(DcMotorEx.class,"lightning");
        light2 = hardwareMap.get(DcMotorEx.class,"odometrLeft");
        light1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        light2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public DcMotorEx light1;
    public DcMotorEx light2;
}
