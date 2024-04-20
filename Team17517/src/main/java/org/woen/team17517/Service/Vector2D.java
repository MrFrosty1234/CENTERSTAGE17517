package org.woen.team17517.Service;

import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.ENC_TO_SM;
import static java.lang.Math.*;

import com.acmerobotics.roadrunner.Vector2d;

public class Vector2D
{
    private double x;
    private double y;
    public Vector2D(double x, double y){
        setCord(x,y);
    }
    public Vector2D(){
        this(0,0);
    }
    public void setCord(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public static double getAngleError(double error){
        while (abs(error)>180) error-=360*signum(error);
        return error;
    }
    public static Vector2D plus(Vector2D vector1, Vector2D vector2){
      return new Vector2D(vector1.x + vector2.x,vector1.y+ vector2.y);
    }
    public void plus(Vector2D vector){
        x = x + vector.getX();
        y = y + vector.getY();
    }
    public Vector2d convertToVector2d(){
        return new Vector2d(y,-x);
    }
    public Vector2D convertToSmFromEnc(){
        x = x*ENC_TO_SM;
        y = y*ENC_TO_SM;
        return this;
    }
    public void copyFrom(Vector2D vector){
        setCord(vector.getX(),vector.getY());
    }
    public void minus(Vector2D vector){
        x = x - vector.getX();
        y = y - vector.getY();
    }
    public void turn(double angle){
        double angle1 = Math.toRadians(angle);
        double x1 = x * cos(angle1) - y * sin(angle1);
        double y1 = x * sin(angle1) + y * cos(angle1);
        x = x1;
        y = y1;
    }

}
