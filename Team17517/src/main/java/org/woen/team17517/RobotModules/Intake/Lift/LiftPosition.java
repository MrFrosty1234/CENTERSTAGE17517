package org.woen.team17517.RobotModules.Intake.Lift;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum LiftPosition {
    DOWN,UP,BACKDROPDOWN, MIDDLE,LOW;
    public static int down = 0;
    public static int up = 1750;
    public static int backdropdown = 750;
    public static int middle = 1300;
    public static int low = 500;
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
            case LOW:
                return low;
        }
    }
}

