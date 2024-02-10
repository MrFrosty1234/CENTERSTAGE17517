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
    private boolean _disable = false;

    @Override
    public void Init(BaseCollector collector) {
        _lighting = Devices.LightingMotor;

        _intake = collector.GetModule(Intake.class);
    }

    @Override
    public void Start() {
        _time.reset();
    }

    public void Disable(){
        _disable = true;
    }

    public void Enable(){
        _disable = false;
    }

    @Override
    public void Update() {
        if(_disable){
            _lighting.setPower(0);

            return;
        }

        if(_intake.isPixelGripped())
            _lighting.setPower(-Configs.Lighting.Brightness);
        else
            _lighting.setPower((Math.sin(Math.pow(_time.seconds(), 1.5) * Configs.Lighting.Intensity) + 1) / 2);
    }
}
