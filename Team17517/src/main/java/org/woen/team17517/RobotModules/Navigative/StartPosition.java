package org.woen.team17517.RobotModules.Navigative;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl;
import org.woen.team17517.Service.Vector2D;

@Config
public enum StartPosition {
    REDSTACKS,REDBACK,BLUESTACK,BLUEBACK;
    public static Vector2D REDSTACKSv = new Vector2D(126d,75.6);
    public static Vector2D REDBACKv   = new Vector2D(126d,-25.2d);
    public static Vector2D BLUESTACKv = new Vector2D(-126d,75.6);
    public static Vector2D BLUEBACKv  = new Vector2D(25.2d,126d);
    public static double REDSTACKSa = -90;
    public static double REDBACKa = -90;
    public static double BLUESTACKa = 90;
    public static double BLUEBACKa = 90;
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
