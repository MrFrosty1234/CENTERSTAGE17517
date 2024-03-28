package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hanging{
    HardwareMap hardwareMap;
    public DcMotorEx hangingMotor;
    public Hanging(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        hangingMotor = hardwareMap.get(DcMotorEx.class, "hang");
    }
}
