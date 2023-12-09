package org.woen.team18742.Modules.Camera;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.woen.team18742.Collectors.BaseCollector;

public class Camera {
    private final WebcamName _camera;

    public Camera(BaseCollector collector){
        _camera = collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam1");
    }

    public RobotPosition GetPosition(){
        return RobotPosition.FORWARD_LEFT;
    }

    public void Start(){

    }

    public void Update(){

    }
}
