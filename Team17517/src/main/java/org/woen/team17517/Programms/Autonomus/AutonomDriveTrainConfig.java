package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team17517.Programms.Autonomus.AutonomBaseClass;
import org.woen.team17517.RobotModules.UltRobot;
@Autonomous
public class AutonomDriveTrainConfig extends AutonomBaseClass {
    @Override
    public Runnable[] getBlueRight(){
        return new Runnable[]{
                ()-> robot.drivetrainNew.setTarget(500,0,0),
                ()-> robot.drivetrainNew.setTarget(-500,0,0),
        };
    }
}