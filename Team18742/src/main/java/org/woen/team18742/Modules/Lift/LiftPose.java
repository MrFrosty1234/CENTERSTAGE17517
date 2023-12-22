package org.woen.team18742.Modules.Lift;

public enum LiftPose {
    UP(560),
    AVERAGE(300),
    DOWN(-1);

    private LiftPose(double pose){
        Pose = pose;
    }

    public double Pose;
}
