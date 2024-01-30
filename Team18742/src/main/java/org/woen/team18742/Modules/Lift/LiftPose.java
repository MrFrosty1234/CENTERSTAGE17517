package org.woen.team18742.Modules.Lift;

import static org.woen.team18742.Tools.Configs.Configs.LiftPoses.*;

public enum LiftPose {
    UP,
    MIDDLE_UPPER,
    MIDDLE_LOWER,
    DOWN;

    public double encoderPose() {
        switch (this) {
            case UP:
                return POSE_UP;
            case MIDDLE_UPPER:
                return POSE_MIDDLE_UPPER;
            case MIDDLE_LOWER:
                return POSE_MIDDLE_LOWER;
            case DOWN:
                return POSE_DOWN;
            default:
                return 0;
        }
    }
}
