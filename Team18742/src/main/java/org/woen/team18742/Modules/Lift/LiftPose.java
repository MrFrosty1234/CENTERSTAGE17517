package org.woen.team18742.Modules.Lift;

public enum LiftPose {
    UP(57),
    DOWN(0);

    public double Pose;

    private LiftPose(double pose){
        Pose = pose;
    }
}
