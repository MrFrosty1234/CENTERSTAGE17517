package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Devices;

@AutonomModule
public class VisionPortalHandler implements IRobotModule {
    private VisionPortal _visualPortal;

    @Override
    public void Init(BaseCollector collector){
        Camera camera = (Camera)collector.GetModule(Camera.class);
        CameraStreamSource video = camera.GetProcessor();

        Odometry odometry = (Odometry) collector.GetModule(Odometry.class);

        _visualPortal = new VisionPortal.Builder().addProcessors(odometry.GetProcessor(), (VisionProcessor) video).setCamera(Devices.Camera).build();

        FtcDashboard.getInstance().startCameraStream(video, 15);
    }

    @Override
    public void Start() {}

    @Override
    public void Update() {}

    @Override
    public void Stop(){
        _visualPortal.close();
    }
}
