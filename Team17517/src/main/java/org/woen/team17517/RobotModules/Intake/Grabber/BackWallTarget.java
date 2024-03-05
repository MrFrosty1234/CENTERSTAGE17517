package org.woen.team17517.RobotModules.Intake.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum BackWallTarget {
    OPEN,CLOSE;
    public static double openValue = 0.5;
    public static double closeValue = 0.5;
    public double get(){
        switch (this){
            case OPEN:
                return openValue;
            case CLOSE:
                return closeValue;
            default:
                return -1;
        }

    }
}
