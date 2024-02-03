package org.woen.team17517.RobotModules.Lift;

public enum LiftPosition {
    DOWN(10), UP(1144), UNKNOWN(0), FORAUTONOM(1000);
    public int value;

    LiftPosition(int value) {
        this.value = value;
    }
}
