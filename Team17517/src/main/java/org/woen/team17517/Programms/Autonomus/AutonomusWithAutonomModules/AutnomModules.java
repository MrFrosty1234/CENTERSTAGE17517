package org.woen.team17517.Programms.Autonomus.AutonomusWithAutonomModules;

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
                () -> robot.grabber.brushOut(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.brushIn(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.close(),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.grabber.brushOut(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.brushOff(),
                () -> robot.timer.getTimeForTimer(0.1),
        });
    }
    public void cameraMove() {
        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(robot.linearOpMode.hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();
        visionPortal.close();
        robot.linearOpMode.telemetry.addData("Pos",pipeLine);
        if (pipeLine.pos == 1){
            robot.updateWhilePositionFalse( new Runnable[]{
                    () -> robot.autnomModules.move(20000,-30000,0,0.6),
                    () -> robot.autnomModules.move(-20000,30000,0,0.6)
                    }

            );
        }else if (pipeLine.pos == 2){
            robot.updateWhilePositionFalse( new Runnable[]{
                    () -> robot.autnomModules.move(0,-30000,0,0.8),
                    () -> robot.autnomModules.move(0,30000,0,0.8)
            }
            );
        }else if (pipeLine.pos == 3){
            robot.updateWhilePositionFalse( new Runnable[]{
                    () -> robot.autnomModules.move(-20000,-30000,-6000,0.6),
                    () -> robot.autnomModules.move(20000,30000,6000,0.6)
            }
            );
        }



    }
}
