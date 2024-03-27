package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hanging {
    HardwareMap hardwareMap;
    public DcMotor hangingMotor;
    public Hanging(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        hangingMotor = hardwareMap.get(DcMotor.class, "hangingMotor");
    }
}
