package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;

public class Odometers{
    HardwareMap hardwareMap;
    public Odometers(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        odometrX = hardwareMap.get(DcMotorEx.class,"right_back_drive");
        odometrLeftY = hardwareMap.get(DcMotorEx.class,"odometrLeft");
        odometrRightY = hardwareMap.get(DcMotorEx.class,"right_front_drive");
        reset();
        directionMap.put(odometrX,1);
        directionMap.put(odometrLeftY,1);
        directionMap.put(odometrRightY,1);
    }
    private void reset(){
        odometrX.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometrLeftY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometrRightY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public double getVelocity(DcMotorEx odometer){
        return odometer.getCurrentPosition()*directionMap.get(odometer);
    }
    public double getPosition(DcMotorEx odometer){
        return odometer.getCurrentPosition()*directionMap.get(odometer);
    }
    public void setDirection(DcMotorEx odometer,int direction){
        directionMap.put(odometer, direction);
    }
    private HashMap<DcMotorEx,Integer>  directionMap = new HashMap();
    public DcMotorEx odometrLeftY;
    public DcMotorEx odometrRightY;
    public DcMotorEx odometrX;

}

