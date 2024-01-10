package org.woen.team18742.Modules.Lift;

public enum LiftPose {
    UP(600),
    AVERAGE(300),
    DOWN(-10);

    private LiftPose(double pose){
        Pose = pose;
    }

    public double Pose;
}
