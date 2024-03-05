package org.woen.team17517.RobotModules.Intake.Grabber;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum GrabberPosition {
    FINISH,SAFE,DOWN;
    public static double safe = 0.5;
    public static double down = 0.97;
    public static double finish = 0.425;
    public double get(){
        switch (this){
            default:
            case DOWN:
                return  down;
            case SAFE:
                return safe;
            case FINISH:
                return finish;

        }
    }
}
