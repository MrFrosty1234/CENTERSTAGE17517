package org.woen.team17517.Robot;

import static java.lang.Math.*;

import java.net.PortUnreachableException;

public class Vector2D
{
    public double x;
    public double y;
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
    public double vectorRadius(double x, double y){
        return Math.sqrt(y*y+x*x);
    }
    public double vectorRadians(double x, double y){
        return Math.atan2(y,x);
    }
    public static Vector2D vectorRadiusAndAngle(double radius, double angle){
        return new Vector2D(cos(angle)*radius,sin(angle)*radius);
    }
    public Vector2D vectorSum(Vector2D vector1, Vector2D vector2){
      return new Vector2D(vector1.x + vector2.x,vector1.y+ vector2.y);
    }

    public Vector2D vectorRat(double angle){
        double radius = vectorRadius(x,y);
        double angleNew = vectorRadians(x,y)-angle;
        double xNew = cos(angleNew*radius);
        double yNew = sin(angleNew*radius);
        return new Vector2D(xNew,yNew);
    }

}
