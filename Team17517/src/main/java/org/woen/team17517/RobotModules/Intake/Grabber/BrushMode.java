package org.woen.team17517.RobotModules.Intake.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum BrushMode {
    IN,OUT,OFF;
    public static double in = 1;
    public static double out = -1;
    public static double of = 0;
    public double get(){
        switch (this){
            case IN:
                return in;
            default:
            case OFF:
                return of;
            case OUT:
                return out;
        }
    }

}
