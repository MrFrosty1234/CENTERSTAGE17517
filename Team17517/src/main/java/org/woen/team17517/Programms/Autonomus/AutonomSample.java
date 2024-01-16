package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutonomSample extends AutonomBaseClass {
    @Override
    public Runnable[] getBlueRight() {
        return new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1400, 0),
                () -> robot.timer.getTimeForTimer(2.3d/1.5d),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, -500),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                ()-> robot.timer.getTimeForTimer(0.2),
                ()-> robot.driveTrainVelocityControl.moveRobotCord(0, 1500, 0),
                () -> robot.timer.getTimeForTimer(4400d/1.5d),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0)
        };
        }


    @Override
    public Runnable[] getRedLeft() {
        return new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(2.3),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 500),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(4400),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0)
        };
    }

    @Override
    public Runnable[] getRedRight() {
        return new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1370, 0),
                () -> robot.timer.getTimeForTimer(2.2d/1.5d),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, -500 *(12/robot.voltageSensorPoint.getVol())),
                () -> robot.timer.getTimeForTimer(2),

                () -> robot.driveTrainVelocityControl.moveRobotCord(0,-1000,0),
                () -> robot.timer.getTimeForTimer(5.2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,1000,0),
                () -> robot.timer.getTimeForTimer(0.6),
                () -> robot.driveTrainVelocityControl.moveRobotCord(1000,0,0),
                () -> robot.timer.getTimeForTimer(0.4),
                () -> robot.driveTrainVelocityControl.moveRobotCord(1000,0,0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,-1000,0),
                () -> robot.timer.getTimeForTimer(0.5),


                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(1.5),
                () -> robot.grabber.perekidFinish(),
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1.5),
                () -> robot.grabber.perekidStart(),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,1000,0),
                () -> robot.timer.getTimeForTimer(0.2),


        };
    }

    @Override
    public Runnable[] getBlueLeft() {
        return new Runnable[]{
                () ->
                        robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () ->
                        robot.timer.getTimeForTimer(1200),
                () ->
                        robot.driveTrainVelocityControl.moveRobotCord(0, 0, 500),
                () ->
                        robot.timer.getTimeForTimer(200),
                () ->
                        robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () ->
                        robot.timer.getTimeForTimer(2400)
        };
    }
}
