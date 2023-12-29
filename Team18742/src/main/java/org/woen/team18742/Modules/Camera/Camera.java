package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.woen.team18742.Tools.Configs;

@Config
public class Camera {
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
