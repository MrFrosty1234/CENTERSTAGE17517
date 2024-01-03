package org.woen.team18742.Modules.Camera;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.RobotModule;
import org.woen.team18742.Tools.Configs.Configs;

@AutonomModule
public class Camera extends RobotModule {
    private final PipeLine pipeLine = new PipeLine();

    public CameraStreamSource GetProcessor() {
        return pipeLine;
    }

    private CameraRobotPosition GetEnum(int val) {
        switch (val) {
            case 1:
                return CameraRobotPosition.LEFT;
            case 2:
                return CameraRobotPosition.FORWARD;
            default:
                return CameraRobotPosition.RIGHT;
        }
    }

    public CameraRobotPosition GetPosition() {
        if (Configs.GeneralSettings.IsCameraDebug)
            return GetEnum(Configs.Camera.RobotPos);

        return GetEnum(pipeLine.pos.get());
    }
}
