package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import org.woen.team18742.Tools.Vector2;

public enum StartRobotPosition {
    RED_BACK(new Vector2(-80.8, -177.6), PI / 2),
    RED_FORWARD(new Vector2(52, -185.7), PI / 2),
    BLUE_BACK(new Vector2(-98.7, 157.1), -PI / 2),
    BLUE_FORWAD(new Vector2(32.0, 164.8), -PI / 2);

    private StartRobotPosition(Vector2 vector, double rotation){
        Position = vector;
        Rotation = rotation;
    }

    public Vector2 Position;
    public double Rotation;
}
