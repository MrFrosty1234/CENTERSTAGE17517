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
        progibServo = robot.hardware.grabberServos.grabberServo;
        openServo = robot.hardware.grabberServos.upServo;
        brushMotor = robot.hardware.intakeAndLiftMotors.brushMotor;
    }
    public GrabberPosition getTargetProgib(){
        return targetProgib;
    }
    public BrushMode getBrushMode() {
        return brushMode;
    }
    public GrabberOpenClosePosition getTargetOpenClose(){
        return targetOpenClose;
    }
    public void finish(){
        targetProgib = GrabberPosition.FINISH;
    }
    public void down(){
        targetProgib = GrabberPosition.DOWN;
    }
    public void safe(){
        targetProgib = GrabberPosition.SAFE;
    }
    public void open(){
        targetOpenClose = GrabberOpenClosePosition.OPEN;
    }
    public void close(){
        targetOpenClose = GrabberOpenClosePosition.CLOSE;
    }
    public void brushIn(){
        brushMode = BrushMode.IN;
    }
    public void brushOut(){
        brushMode = BrushMode.OUT;
    }
    public void brushOff(){
        brushMode = BrushMode.OFF;
    }
    private BrushMode brushMode = BrushMode.OFF;
    private GrabberPosition targetProgib = GrabberPosition.DOWN;
    private GrabberOpenClosePosition targetOpenClose = GrabberOpenClosePosition.OPEN;
    public void update(){
        progibServo.setPosition(targetProgib.value);
        openServo.setPosition(targetOpenClose.value);
        brushMotor.setPower(brushMode.value);
    }
}
