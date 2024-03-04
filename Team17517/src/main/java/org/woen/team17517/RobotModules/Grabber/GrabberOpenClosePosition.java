package org.woen.team17517.RobotModules.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum GrabberOpenClosePosition {
    OPEN,CLOSE;
    public static double open  = 0.45;
    public static double close = 0.3;
    public double get(){
        switch (this){
            case CLOSE:
                return close;
            default:
            case OPEN:
                return open;
        }
    }
}
