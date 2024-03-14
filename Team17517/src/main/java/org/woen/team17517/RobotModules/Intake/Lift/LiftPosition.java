package org.woen.team17517.RobotModules.Intake.Lift;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum LiftPosition {
    DOWN,UP,BACKDROPDOWN, MIDDLE;
    public static int down = 0;
    public static int up = 2725;
    public static int backdropdown =1050;
    public static int middle = 1700;
    public int get(){
        switch (this){
            default:
            case DOWN:
                return down;
            case UP:
                return up;
            case BACKDROPDOWN:
                return backdropdown;
            case MIDDLE:
                return middle;
        }
    }
}

