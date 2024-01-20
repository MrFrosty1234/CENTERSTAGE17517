package org.woen.team17517.NotUsedCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team17517.Programms.Autonomus.AutonomBaseClass;
import org.woen.team17517.RobotModules.UltRobot;
@Autonomous
public class AutonomDriveTrainConfig extends AutonomBaseClass {

    public Runnable[] getBlueRight(){
        return new Runnable[]{
                ()-> robot.driveTrainVelocityControl.moveGlobalCord(0,00,500),
                ()->robot.timer.getTimeForTimer(1)
        };
    }
}