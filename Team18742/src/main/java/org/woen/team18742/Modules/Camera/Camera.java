package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.woen.team18742.OpenCV.PipeLine;

@Config
public class Camera {
    public static boolean IsDebug = true;
    public static int RobotPos = 2;
    PipeLine pipeLine = new PipeLine();

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
        if (IsDebug)
            return GetEnum(RobotPos);

        return GetEnum(pipeLine.pos.get());
    }
}
