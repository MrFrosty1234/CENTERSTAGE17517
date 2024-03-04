package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeAndLiftMotors{
    HardwareMap hardwareMap;
    public IntakeAndLiftMotors(HardwareMap hardwareMap){this.hardwareMap = hardwareMap;
        brushMotor = hardwareMap.get(DcMotorEx.class,"intakeMotor");
        liftMotor  = hardwareMap.get(DcMotorEx.class,"liftMotor");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public DcMotorEx brushMotor;
    public DcMotorEx liftMotor;
}
