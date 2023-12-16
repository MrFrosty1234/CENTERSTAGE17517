package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Tools.Devices;

public class VisionPortalHandler {
    private final VisionPortal _visualPortal;

    public VisionPortalHandler(VisionProcessor[] processors){
        _visualPortal = new VisionPortal.Builder().addProcessors(processors).setCamera(Devices.Camera).build();
    }

    public void StartDashBoardVid(CameraStreamSource source){
     //   FtcDashboard.getInstance().startCameraStream(source, 10);
    }

    public void Stop(){
        _visualPortal.close();
    }
}
