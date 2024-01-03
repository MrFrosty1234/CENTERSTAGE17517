package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.RobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Devices;

@AutonomModule
public class VisionPortalHandler extends RobotModule {
    private VisionPortal _visualPortal;

    @Override
    public void Init(BaseCollector collector){
        CameraStreamSource video = collector.GetModule(Camera.class).GetProcessor();

        _visualPortal = new VisionPortal.Builder().addProcessors(collector.GetModule(Odometry.class).GetProcessor(), (VisionProcessor) video).setCamera(Devices.Camera).build();

        FtcDashboard.getInstance().startCameraStream(video, 15);
    }

    @Override
    public void Stop(){
        _visualPortal.close();
    }
}
