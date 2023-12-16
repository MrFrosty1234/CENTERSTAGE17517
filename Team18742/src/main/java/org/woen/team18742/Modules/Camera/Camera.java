package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.OpenCV.PipeLine;

@Config
public class Camera {
    public static boolean IsDebug = true;
    public static int RobotPos = 2;
    PipeLine pipeLine = new PipeLine();

    public CameraStreamSource GetProcessor() {
        return pipeLine;
    }

    private RobotPosition GetEnum(int val) {
        switch (val) {
            case 1:
                return RobotPosition.LEFT;
            case 2:
                return RobotPosition.FORWARD;
            default:
                return RobotPosition.RIGHT;
        }
    }

    public RobotPosition GetPosition() {
        if (IsDebug)
            return GetEnum(RobotPos);

        return GetEnum(pipeLine.pos.get());
    }
}
