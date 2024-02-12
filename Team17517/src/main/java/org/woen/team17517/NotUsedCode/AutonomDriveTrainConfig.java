package org.woen.team17517.NotUsedCode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team17517.NotUsedCode.OldAutonomus.AutonomBaseClass;

@Autonomous
@Config
public class AutonomDriveTrainConfig extends AutonomBaseClass {
    public static double x = 0;
    public static double y = 0;
    public static double targetH = 0;
    public Runnable[] getBlueNearBack(){
        return new Runnable[]{
                ()-> robot.driveTrainVelocityControl.moveRobotCord(x,y,targetH),
                ()->robot.timer.getTimeForTimer(1),
                ()-> robot.driveTrainVelocityControl.moveRobotCord(-x,-y,-targetH),
                ()->robot.timer.getTimeForTimer(1)
        };
    }
}