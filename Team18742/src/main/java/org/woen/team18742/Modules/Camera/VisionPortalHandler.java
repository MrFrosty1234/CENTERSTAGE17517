package org.woen.team18742.Modules.Camera;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Tools.Devices;

public class VisionPortalHandler {
    private final VisionPortal _visualPortal;

    public VisionPortalHandler(VisionProcessor[] processors){
        _visualPortal = new VisionPortal.Builder().addProcessors(processors).setCamera(Devices.Camera).build();
    }

    public void Stop(){
        _visualPortal.close();
    }
}
