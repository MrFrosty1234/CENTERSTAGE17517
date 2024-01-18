package org.woen.team18742.Modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.CVOdometry;
import org.woen.team18742.Tools.Devices;

@Module
public class VisionPortalHandler implements IRobotModule {
    private VisionPortal _visualPortal;

    @Override
    public void Init(BaseCollector collector){
        if(collector instanceof AutonomCollector) {
            CameraStreamSource video = collector.GetModule(Camera.class).GetProcessor();

            _visualPortal = new VisionPortal.Builder().addProcessors((VisionProcessor) video, collector.GetModule(CVOdometry.class).GetProcessor()).setCamera(Devices.Camera).build();

            FtcDashboard.getInstance().startCameraStream(video, 15);
        }
        else
            _visualPortal = new VisionPortal.Builder().addProcessors(collector.GetModule(CVOdometry.class).GetProcessor()).setCamera(Devices.Camera).build();
    }

    @Override
    public void Stop(){
        while (_visualPortal.getCameraState() == VisionPortal.CameraState.OPENING_CAMERA_DEVICE);

        _visualPortal.close();
        FtcDashboard.getInstance().stopCameraStream();
    }

    public boolean IsCameraOpened(){
        return _visualPortal.getCameraState() == VisionPortal.CameraState.CAMERA_DEVICE_READY;
    }
}
