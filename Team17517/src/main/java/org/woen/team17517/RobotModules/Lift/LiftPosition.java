package org.woen.team17517.RobotModules.Lift;

public enum LiftPosition {
    DOWN(0), UP(950),BACKDROPDOWN(400);
    public int value;

    LiftPosition(int value) {
        this.value = value;
    }
}

