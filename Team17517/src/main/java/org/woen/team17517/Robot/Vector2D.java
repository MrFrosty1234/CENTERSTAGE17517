package org.woen.team17517.Robot;

import static java.lang.Math.*;

import java.net.PortUnreachableException;

public class Vector2D {
    private double x;
    private double y;
    public Vector2D(double x, double y){
        setCord(x,y);
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

    public Vector2D vectorRat(Vector2D vector,double angle){

        double radius = vectorRadius(vector.x,vector.y);
        double angleNew = vectorRadians(vector.x,vector.y)-angle;
        double xNew = cos(angleNew*radius);
        double yNew = Math.sin(angleNew*radius);
        return new Vector2D(xNew,yNew);
    }

}
