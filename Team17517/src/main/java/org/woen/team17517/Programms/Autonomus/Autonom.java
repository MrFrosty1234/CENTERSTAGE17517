package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Autonom extends AutonomBaseClass {
    @Override
    public Runnable[] getRedLeft() {
        return new Runnable[]{
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(25000, 0, 20),
                () -> robot.timer.getTimeForTimer(1.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -24000, 0),
                () -> robot.timer.getTimeForTimer(5.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 12000, 0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(-12000, 0, 0),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -12000, 0),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.grabber.perekidFinish(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.closeGraber(),
                () -> robot.timer.getTimeForTimer(1),


        };
    }

    @Override
    public Runnable[] getBlueLeft() {
        return new Runnable[]{
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(-24000, 0, -20),
                () -> robot.timer.getTimeForTimer(1.3),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -24000, 0),
                () -> robot.timer.getTimeForTimer(5.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 12000, 0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(12000, 0, 0),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -12000, 0),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(3),
                () -> robot.grabber.perekidFinish(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.closeGraber(),
                () -> robot.timer.getTimeForTimer(1),
        };
    }
    @Override
    public Runnable[] getRedRight(){
        return new Runnable[]{
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(24000,0,0),
                () -> robot.timer.getTimeForTimer(0.7),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,0,0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,-15000,0),
                () -> robot.timer.getTimeForTimer(5),
                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.perekidFinish(),
                () -> robot.timer.getTimeForTimer(0.3),
                () -> robot.grabber.closeGraber(),
                () -> robot.timer.getTimeForTimer(0.3),
        };
    }
    @Override
    public Runnable[] getBlueRight(){
        return new Runnable[]{
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(-25000,0,0),
                () -> robot.timer.getTimeForTimer(0.7),
                () -> robot.driveTrainVelocityControl.moveRobotCord(14000,0,0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,0,0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,-22000,0),
                () -> robot.timer.getTimeForTimer(5),
                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.perekidFinish(),
                () -> robot.timer.getTimeForTimer(0.3),
                () -> robot.grabber.closeGraber(),
                () -> robot.timer.getTimeForTimer(0.3),
                () -> robot.grabber.perekidStart(),
                () -> robot.timer.getTimeForTimer(0.3),

                //////////////////////////
                /*
                () -> robot.driveTrainVelocityControl.moveRobotCord(-18000,0,0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(26000,0,0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.grabber.pixelMotor.setPower(-1),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.grabber.openGraber(),
                () -> robot.driveTrainVelocityControl.moveRobotCord(-24000,0,0),
                () -> robot.timer.getTimeForTimer(2),

                 */

        };
    }
}



