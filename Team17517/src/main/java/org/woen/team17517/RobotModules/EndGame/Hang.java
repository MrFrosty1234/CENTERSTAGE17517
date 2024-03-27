package org.woen.team17517.RobotModules.EndGame;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Hang implements RobotModule {
    UltRobot robot;
    public DcMotor hangingMotor;
    public Hang(UltRobot robot){
        this.robot = robot;
        hangingMotor = robot.hardware.hanging.hangingMotor;
    }
    private HangPower hangPower = HangPower.ZERO;
    @Override
    public void update() {
       hangingMotor.setPower(hangPower.get());
    }
}
