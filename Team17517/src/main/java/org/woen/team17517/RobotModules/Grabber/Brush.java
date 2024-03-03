package org.woen.team17517.RobotModules.Grabber;

import static org.woen.team17517.RobotModules.Grabber.BrushMode.*;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Brush implements RobotModule{
    DcMotorEx motor;
    public Brush(UltRobot robot){
        motor = robot.hardware.intakeAndLiftMotors.brushMotor;
    }
    BrushMode target = OFF;
    public void in(){target = IN;}
    public void out(){target = OUT;}
    public void off(){target = OFF;}
    public void update(){
        motor.setPower(target.get());
    }
}
