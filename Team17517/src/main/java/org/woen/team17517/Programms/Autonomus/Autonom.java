package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class Autonom extends AutonomBaseClass{
    @Override
    public  Runnable[] getBlueRight(){
        return new Runnable []{()-> robot.driveTrainVelocityControl.moveRobotCord(0,1000,0),
                            ()-> robot.timer.getTimeForTimer(1),
                            ()-> robot.driveTrainVelocityControl.moveRobotCord(0,-1000,0),
                            ()-> robot.timer.getTimeForTimer(1)};
    }
    @Override
    public  Runnable[] getBlueLeft(){
        return new Runnable[]{};
    }
    @Override
    public  Runnable[] getRedRight(){
        return new Runnable[]{};
    }
    @Override
    public  Runnable[] getRedLeft(){
        return new Runnable[]{};
    }
}
