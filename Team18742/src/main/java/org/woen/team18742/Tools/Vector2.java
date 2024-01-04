package org.woen.team18742.Tools;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import com.acmerobotics.roadrunner.Vector2d;

public class Vector2 {
    public double X;
    public double Y;

    public Vector2(){
        X = 0;
        Y = 0;
    }

    public Vector2(double x, double y){
        X = x;
        Y = y;
    }

    public Vector2(Vector2d vec){
        X = vec.x;
        Y = vec.y;
    }

    public double Abs(){
        return sqrt(X * X + Y * Y);
    }

    public static Vector2 Plus(Vector2 vec1, Vector2 vec2){
        return new Vector2(vec1.X + vec2.X, vec1.Y + vec2.Y);
    }

    public static Vector2 Minus(Vector2 vec1, Vector2 vec2){
        return new Vector2(vec1.X - vec2.X, vec1.Y - vec2.Y);
    }

    public Vector2 Turn(double rotate){
        return new Vector2(cos(rotate) * X - sin(rotate) * Y, sin(rotate) * X + cos(rotate) * Y);
    }

    public static Vector2 Multiply(Vector2 vec1, Vector2 vec2){
        return new Vector2(vec1.X * vec2.X, vec1.Y * vec2.Y);
    }

    public static Vector2 ToVector(double val){
        return new Vector2(val, val);
    }

    public Vector2 copy(){
        return new Vector2(X, Y);
    }

    @Override
    public String toString(){
        return "X = " + X + "Y = " + Y;
    }
}
