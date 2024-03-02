package org.woen.team17517.RobotModules.Lift;

import com.acmerobotics.dashboard.config.Config;

@Config
public enum LiftPosition {
    DOWN,UP,BACKDROPDOWN;
    public static int down;
    public static int up;
    public static int backdropdown;
    public int get(){
        switch (this){
            default:
            case DOWN:
                return down;
            case UP:
                return up;
            case BACKDROPDOWN:
                return backdropdown;
        }
    }
}

