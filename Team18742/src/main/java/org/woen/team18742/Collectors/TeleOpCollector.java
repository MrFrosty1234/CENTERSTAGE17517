package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Modules.Camera.PipeLine;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Manager.TeleopModule;
import org.woen.team18742.Modules.Manual;

public class TeleOpCollector extends BaseCollector {
    public TeleOpCollector(LinearOpMode robot) {
        super(robot);

        AddAdditionModules(AnnotationFinder.GetAnnotation(TeleopModule.class));
    }
}