package org.woen.team18742.Modules.Lift;

public enum LiftPose {
    UP(600),
    AVERAGE(200),
    MEGA_AVERAGE(400),
    DOWN(-40);

    private LiftPose(double pose){
        Pose = pose;
    }

    public double Pose;
}
