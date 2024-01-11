package org.woen.team18742.Modules.Camera;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.ToolTelemetry;

@AutonomModule
public class Camera implements IRobotModule {
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

    @Override
    public void Update() {
        ToolTelemetry.AddLine("Camera = " + GetPosition());
    }
}
