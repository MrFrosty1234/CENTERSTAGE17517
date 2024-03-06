package org.woen.team17517.RobotModules.Intake.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum PurplePixelServo {
    OPEN, CLOSE;
    public static double openServo = 0;
    public static double closeSevo = 1;
    public double get(){
        switch (this){
            case OPEN:
                return openServo;
            case CLOSE:
                return closeSevo;
            default:
                return -1;
        }
    }
}
