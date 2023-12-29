package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Modules.Camera.PipeLine;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Manual;

public class TeleOpCollector extends BaseCollector {
    public org.woen.team18742.Modules.Manual Manual;

    public TeleOpCollector(LinearOpMode commandCode) {
        super(commandCode);

        Manual = new Manual(this);
    }

    @Override
    public void Update() {
        super.Update();
        Manual.Update();
    }

    @Override
    public void Start() {
        super.Start();
        Intake.setGripper(false);
    }

    @Override
    public void Stop() {
        super.Stop();
    }
}