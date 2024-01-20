package org.woen.team17517.NotUsedCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team17517.Programms.Autonomus.AutonomBaseClass;
import org.woen.team17517.RobotModules.UltRobot;


public class AutonomBase extends AutonomBaseClass {
    UltRobot robot;
    @Override
    public Runnable[] getBlueRight(){
        robot = new UltRobot(this);
        return new Runnable[]{
                ()->robot.drivetrainNew.setTarget(60,0,0),
                ()->robot.timer.getTimeForTimer(0.1),
                ()->robot.drivetrainNew.setTarget(-60,0,0),
                ()->robot.timer.getTimeForTimer(0.1)
        };
    }

}
