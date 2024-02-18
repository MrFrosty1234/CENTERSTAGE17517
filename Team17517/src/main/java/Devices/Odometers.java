package Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Odometers{
    HardwareMap hardwareMap;
    public Odometers(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        odometrX = hardwareMap.get(DcMotorEx.class,"right_back_drive");
        odometrLeftY = hardwareMap.get(DcMotorEx.class,"odometrLeft");
        odometrRightY = hardwareMap.get(DcMotorEx.class,"right_front_drive");
        reset();
    }
    private void reset(){
        odometrX.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometrLeftY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometrRightY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public DcMotorEx odometrLeftY;
    public DcMotorEx odometrRightY;
    public DcMotorEx odometrX;

}

