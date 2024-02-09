package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module
public class Lighting implements IRobotModule {
    private Intake _intake;

    private DcMotor _lighting;

    private final ElapsedTime _time = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _lighting = Devices.LightingMotor;

        _intake = collector.GetModule(Intake.class);
    }

    @Override
    public void Start() {
        _time.reset();
    }

    @Override
    public void Update() {
        if(_intake.isPixelGripped())
            _lighting.setPower(-Configs.Lighting.Brightness);
        else
            _lighting.setPower((Math.sin(Math.pow(_time.seconds(), 1.5) * Configs.Lighting.Intensity) + 1) / 2);
    }
}
