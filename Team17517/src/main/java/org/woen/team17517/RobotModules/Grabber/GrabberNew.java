package org.woen.team17517.RobotModules.Grabber;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class GrabberNew implements RobotModule {
    UltRobot robot;
    private Servo progibServo;
    private Servo openServo;
    private DcMotorEx brushMotor;
    public GrabberNew(UltRobot robot){
        this.robot = robot;
        progibServo = robot.devices.progibServo;
        openServo = robot.devices.pixelServoLeft;
        brushMotor = robot.devices.intakeMotor;
    }
    public double getTargetProgib(){
        return targetProgib;
    }
    public double getTargetOpenClose(){
        return targetOpenClose;
    }
    public void finish(){
        targetProgib = GrabberPosition.FINISH.value;
    }
    public void down(){
        targetProgib = GrabberPosition.DOWN.value;
    }
    public void safe(){
        targetProgib = GrabberPosition.SAFE.value;
    }
    public void open(){
        targetOpenClose = GrabberOpenClosePosition.OPEN.value;
    }
    public void close(){
        targetOpenClose = GrabberOpenClosePosition.CLOSE.value;
    }
    public void brushIn(){
        brushMode = BrushMode.IN.value;
    }
    public void brushOut(){
        brushMode = BrushMode.OUT.value;
    }
    public void brushOff(){
        brushMode = BrushMode.OFF.value;
    }
    private double brushMode = BrushMode.OFF.value;
    private double targetProgib = GrabberPosition.DOWN.value;
    private double targetOpenClose = GrabberOpenClosePosition.OPEN.value;
    public void update(){
        progibServo.setPosition(targetProgib);
        openServo.setPosition(targetOpenClose);
        brushMotor.setPower(brushMode);
    }
}
