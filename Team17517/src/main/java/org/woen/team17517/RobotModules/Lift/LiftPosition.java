package org.woen.team17517.RobotModules.Lift;

public enum LiftPosition {
    DOWN(0), UP(2733), UNKNOWN(0), FORAUTONOM(1000);
    public int value;

    LiftPosition(int value) {
        this.value = value;
    }
}
