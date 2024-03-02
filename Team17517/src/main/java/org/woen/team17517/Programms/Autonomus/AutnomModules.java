package org.woen.team17517.Programms.Autonomus;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

public class AutnomModules {
    UltRobot robot;
    PipeLine pipeLine;
    public AutnomModules(UltRobot robot){
        this.robot = robot;
    }
    public void move(double x, double y, double h, double time){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.driveTrainVelocityControl.moveRobotCord(x,y,h),
                ()->robot.timer.getTimeForTimer(time),
                ()->robot.driveTrainVelocityControl.moveRobotCord(0,0,0),
                ()->robot.timer.getTimeForTimer(0.1),

        });
    }
    public void bacBoardPixels(){
        robot.updateWhilePositionFalse(new Runnable[]{
                ()->robot.grabber.close(),
                ()->robot.grabber.safe(),
                ()->robot.timer.getTimeForTimer(0.2),
                ()->robot.lift.moveUP(),
                ()-> move(0,20000,0,1),
                ()->robot.grabber.finish(),
                ()-> move(0,-20000,0,1),
                ()->robot.grabber.open(),
                ()->robot.timer.getTimeForTimer(0.5),
                ()->robot.grabber.safe(),
                ()->robot.lift.moveDown(),
                ()->robot.grabber.down()
        });
    }
    public void eatPixels() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.in(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.close(),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.off(),
                () -> robot.timer.getTimeForTimer(0.1),
        });
    }
}
