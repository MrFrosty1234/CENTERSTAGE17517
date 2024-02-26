package org.woen.team18742.Modules.Intake;

import org.woen.team18742.Tools.Configs.Configs;

public enum GripperStates {
    OPEN,
    ONE_GRIPPED,
    ALL_GRIPPED;

    public double getServoPosition(){
        switch (this){
            case OPEN:
                return Configs.Intake.servoGripperNormal;

            case ALL_GRIPPED:
                return Configs.Intake.servoGripperGripped;

            case ONE_GRIPPED:
                return Configs.Intake.servoGripperGrippedOne;
        }

        throw new RuntimeException("attempt to find non-existent gripper position");
    }
}
