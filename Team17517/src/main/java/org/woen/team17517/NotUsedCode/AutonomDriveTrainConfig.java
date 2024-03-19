package org.woen.team17517.NotUsedCode;

import org.woen.team17517.NotUsedCode.OldAutonomus.AutonomBaseClass;

public class AutonomDriveTrainConfig extends AutonomBaseClass {
    public static double x = 0;
    public static double y = 0;
    public static double targetH = 0;
    public Runnable[] getBlueNearBack(){
        return new Runnable[]{
                ()-> robot.driveTrainVelocityControl.moveRobotCord(x,y,targetH),
                ()->robot.timer.waitSeconds(1),
                ()-> robot.driveTrainVelocityControl.moveRobotCord(-x,-y,-targetH),
                ()->robot.timer.waitSeconds(1)
        };
    }
}