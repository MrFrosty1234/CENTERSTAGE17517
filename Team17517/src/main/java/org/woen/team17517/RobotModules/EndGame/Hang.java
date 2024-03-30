package org.woen.team17517.RobotModules.EndGame;

import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Hang implements RobotModule {
    UltRobot robot;
    public DcMotorEx hangingMotor;
    private static HangPower hangPower = HangPower.ZERO;
    public Hang(UltRobot robot){
        this.robot = robot;
        hangingMotor = robot.hardware.hanging.hangingMotor;
    }
    public void up(){
        hangPower = HangPower.UP;
    }
    public void setHangPower(HangPower power){hangPower = power;}
    public HangPower getState(){return hangPower;}
    @Override
    public void update() {hangingMotor.setPower(hangPower.get());}
}

