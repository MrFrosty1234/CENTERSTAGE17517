package org.woen.team17517.Service;

import static java.lang.Math.*;

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
    public double vectorRadius(double x, double y){
        return Math.sqrt(y*y+x*x);
    }
    public double vectorRadians(double x, double y){
        return Math.atan2(y,x);
    }
    public static double getAngleError(double error){
        while (abs(error)>180) error-=360*signum(error);
        return error;
    }
    public void vectorRadiusAndAngle(double radius, double angle){
         this.x = cos(angle)*radius;
         this.y = sin(angle)*radius;
    }
    public static Vector2D plus(Vector2D vector1, Vector2D vector2){
      return new Vector2D(vector1.x + vector2.x,vector1.y+ vector2.y);
    }
    public void plus(Vector2D vector){
        x = x + vector.getX();
        y = y + vector.getY();
    }

    public void minus(Vector2D vector){
        x = x - vector.getX();
        y = y - vector.getY();
    }
    public void turn(double angle){
        x = x * cos(angle) - y * sin(angle);
        y = x * sin(angle) + y * cos(angle);
    }

}
