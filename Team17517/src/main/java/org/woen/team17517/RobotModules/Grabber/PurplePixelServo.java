package org.woen.team17517.RobotModules.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum PurplePixelServo {
    OPEN, CLOSE;
    public static double openServo = 0;
    public static double closeSevo = 0.5;
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