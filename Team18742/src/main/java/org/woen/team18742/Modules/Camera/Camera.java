package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.OpenCV.PipeLine;

@Config
public class Camera {
    private final WebcamName _camera;
    public static boolean IsDebug = false;
    public static RobotPosition RobotPos;

    VisionPortal visionPortal;
    PipeLine pipeLine = new PipeLine();

    public Camera(BaseCollector collector) {
        _camera = collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam1");

        visionPortal = new VisionPortal.Builder()
                .setCamera(collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeLine)
                .build();

        visionPortal.setProcessorEnabled(pipeLine, false);
    }

    private int Pose = 0;

    public RobotPosition GetPosition() {
        if(IsDebug)
            return RobotPos;

        switch (Pose) {
            case 1:
                return RobotPosition.LEFT;
            case 2:
                return RobotPosition.FORWARD;
            default:
                return RobotPosition.RIGHT;
        }
    }

    public void Start() {
        Pose = pipeLine.pos;
        visionPortal.setProcessorEnabled(pipeLine, true);
    }

    public void Update() {
        Pose = pipeLine.pos;
    }
}
