package org.woen.team17517.RobotModules.Grabber;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class GrabberNew implements RobotModule {
    UltRobot robot;
    private Servo progibServo;
    private Servo openServo;
    private Servo backWall;
    private Servo autonomServo;
    public GrabberNew(UltRobot robot){
        this.robot = robot;
        progibServo = robot.hardware.grabberServos.grabberServo;
        openServo = robot.hardware.grabberServos.upServo;
        backWall = robot.hardware.grabberServos.backWallServo;
        autonomServo = robot.hardware.grabberServos.autonomServo;

    }
    public GrabberPosition getProgibTarget(){
        return progibTarget;
    }
    public GrabberOpenClosePosition getOpenCloseTarget(){
        return openCloseTarget;
    }
    public void finish(){
        progibTarget = GrabberPosition.FINISH;
    }
    public void down(){
        progibTarget = GrabberPosition.DOWN;
    }
    public void safe(){
        progibTarget = GrabberPosition.SAFE;
    }
    public void open(){
        openCloseTarget = GrabberOpenClosePosition.OPEN;
    }
    public void close(){
        openCloseTarget = GrabberOpenClosePosition.CLOSE;
    }
    public void backWallClose(){backWallTarget = BackWallTarget.CLOSE;}
    public void backWallOpen(){backWallTarget = BackWallTarget.OPEN;}
    public BackWallTarget getBackWallTarget(){
        return backWallTarget;
    }
    public void openPurplePixel(){autonomServoTarget = PurplePixelServo.OPEN;}
    private void closePurplepIxel(){autonomServoTarget = PurplePixelServo.CLOSE;}
    private BackWallTarget backWallTarget = BackWallTarget.CLOSE;
    private GrabberPosition progibTarget = GrabberPosition.DOWN;
    private GrabberOpenClosePosition openCloseTarget = GrabberOpenClosePosition.OPEN;
    private PurplePixelServo autonomServoTarget = PurplePixelServo.CLOSE;
    public void update(){
        progibServo. setPosition(progibTarget.get());
        openServo.   setPosition(openCloseTarget.get());
        backWall.    setPosition(backWallTarget.get());
        autonomServo.setPosition(autonomServoTarget.get());
    }
}
