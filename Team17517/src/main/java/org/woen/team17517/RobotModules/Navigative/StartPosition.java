package org.woen.team17517.RobotModules.Navigative;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.Service.Vector2D;

@Config
public enum StartPosition {
    REDSTACKS,REDBACK,BLUESTACK,BLUEBACK;
    public static Vector2D REDSTACKSv = new Vector2D();
    public static Vector2D REDBACKv   = new Vector2D();
    public static Vector2D BLUESTACKv = new Vector2D();
    public static Vector2D BLUEBACKv  = new Vector2D();
    public static double REDSTACKSa = 0;
    public static double REDBACKa = 0;
    public static double BLUESTACKa = 0;
    public static double BLUEBACKa =0;
    public Vector2D getVector(){
        switch (this){
            default:
            case REDBACK:
                return REDBACKv;
            case REDSTACKS:
                return REDSTACKSv;
            case BLUEBACK:
                return BLUEBACKv;
            case BLUESTACK:
                return BLUESTACKv;
        }
    }
    public double getAngle(){
        switch (this){
            default:
            case REDBACK:
                return REDBACKa;
            case REDSTACKS:
                return REDSTACKSa;
            case BLUEBACK:
                return BLUEBACKa;
            case BLUESTACK:
                return BLUESTACKa;
        }
    }
}
