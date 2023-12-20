package org.woen.team18742.Modules;

import org.woen.team18742.Tools.Vector2;

public enum StartRobotPosition {
    RED_BACK(new Vector2()),
    RED_FORWARD(new Vector2()),
    BLUE_BACK(new Vector2()),
    BLUE_FORWAD(new Vector2());

    private StartRobotPosition(Vector2 vector){
        Position = vector;
    }

    public Vector2 Position;
}
