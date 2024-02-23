package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrainMotors{
    HardwareMap hardwareMap;
    public DriveTrainMotors(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        left_front_drive  = hardwareMap.get(DcMotorEx.class,"left_front_drive");
        left_back_drive   = hardwareMap.get(DcMotorEx.class, "left_back_drive");
        right_back_drive  = hardwareMap.get(DcMotorEx.class, "right_back_drive");
        right_front_drive = hardwareMap.get(DcMotorEx.class, "right_front_drive");

        reset();
    }
    public DcMotorEx left_back_drive;
    public DcMotorEx right_front_drive;
    public DcMotorEx right_back_drive;
    public DcMotorEx left_front_drive;

    private void reset(){
        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setDirection(DcMotor.Direction.FORWARD);

        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.REVERSE);

        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        left_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        left_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}

