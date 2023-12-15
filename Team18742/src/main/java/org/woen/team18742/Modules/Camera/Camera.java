package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.OpenCV.PipeLine;
import org.woen.team18742.OpenCV.TestVisionProcessor;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Camera {
    private CameraName _camera;
    public static boolean IsDebug = false;
    public static int RobotPos = 2;

    VisionPortal visionPortal;
    PipeLine pipeLine = new PipeLine();

    public Camera(BaseCollector collector) {
        _camera = collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam 1");

        visionPortal = new VisionPortal.Builder()
                .setCamera(_camera)
                .addProcessor(pipeLine)
                .build();

        FtcDashboard.getInstance().startCameraStream(pipeLine,10);
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

    public void Stop() {
        visionPortal.close();
    }
}
