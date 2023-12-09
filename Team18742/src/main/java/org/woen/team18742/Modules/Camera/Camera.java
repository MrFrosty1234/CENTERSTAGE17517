package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.woen.team18742.Collectors.BaseCollector;

@Config
public class Camera {
    private final WebcamName _camera;
    public static boolean IsDebug = false;
    public static RobotPosition RobotPos;

    public Camera(BaseCollector collector){
        _camera = collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam1");
    }

    public RobotPosition GetPosition(){
        return IsDebug ? RobotPos : RobotPosition.FORWARD_LEFT;
    }

    public void Start(){

    }

    public void Update(){

    }
}
