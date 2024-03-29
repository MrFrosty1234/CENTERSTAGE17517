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
    public void down(){hangPower = HangPower.DOWN;}
    public void stop(){
        hangPower = HangPower.ZERO;
    }
    public HangPower getState(){return hangPower;}
    @Override
    public void update() {
        if(hangPower == HangPower.UP && hangingMotor.getCurrent(CurrentUnit.AMPS)>1.7) {
            hangPower = HangPower.ZERO;
        }else
            hangingMotor.setPower(hangPower.get());
    }
}
