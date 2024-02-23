package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeAndLiftMotors{
    HardwareMap hardwareMap;
    public IntakeAndLiftMotors(HardwareMap hardwareMap){this.hardwareMap = hardwareMap;
        brushMotor = hardwareMap.get(DcMotorEx.class,"intakeMotor");
        liftMotor  = hardwareMap.get(DcMotorEx.class,"liftMotor");}
    public DcMotorEx brushMotor;
    public DcMotorEx liftMotor;
}
