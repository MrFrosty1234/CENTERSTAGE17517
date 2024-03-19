package org.woen.team17517.RobotModules.Navigative;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.Service.Vector2D;

@Config
public enum StartPosition {
    RED_STACKS, RED_BACK, BLUE_STACK, BLUE_BACK,ZERO;
    public static Vector2D RED_STACK_VECTOR = new Vector2D(126d,75.6);
    public static Vector2D RED_BACK_VECTOR = new Vector2D(126d,-25.2d);
    public static Vector2D BLUE_STACK_VECTOR = new Vector2D(-126d,75.6);
    public static Vector2D BLUE_BACK_VECTOR = new Vector2D(25.2d,126d);
    public static final Vector2D ZERO_VECTOR = new Vector2D();
    public static double RED_STACK_ANGLE = -90;
    public static double RED_BACK_ANGLE = -90;
    public static double BLUE_STACK_ANGLE = 90;
    public static double BLUE_BACK_ANGLE = 90;
    public static double ZERO_ANGLE = 0;
    public Vector2D getVector(){
        switch (this){
            default:
            case RED_BACK:
                return RED_BACK_VECTOR;
            case RED_STACKS:
                return RED_STACK_VECTOR;
            case BLUE_BACK:
                return BLUE_BACK_VECTOR;
            case BLUE_STACK:
                return BLUE_STACK_VECTOR;
            case ZERO:
                return ZERO_VECTOR;
        }
    }
    public double getAngle(){
        switch (this){
            default:
            case RED_BACK:
                return RED_BACK_ANGLE;
            case RED_STACKS:
                return RED_STACK_ANGLE;
            case BLUE_BACK:
                return BLUE_BACK_ANGLE;
            case BLUE_STACK:
                return BLUE_STACK_ANGLE;
            case ZERO:
                return ZERO_ANGLE;
        }
    }
}
