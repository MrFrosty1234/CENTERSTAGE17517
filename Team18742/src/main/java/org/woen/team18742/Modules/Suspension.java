package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module
public class Suspension implements IRobotModule {
    private Servo podtyaga1;
    private Servo podtyaga2;

    @Override
    public void Init(BaseCollector collector) {
        podtyaga1 = Devices.podtyaga1;
        podtyaga2 = Devices.podtyaga2;
    }

    public void Active(){
        podtyaga1.setPosition(Configs.Suspension.nulevayapodtyaga1);
        podtyaga2.setPosition(Configs.Suspension.nulevayapodtyaga2);
    }

    public void Disable(){
        podtyaga1.setPosition(Configs.Suspension.rasstrelennayatyga1);
        podtyaga2.setPosition(Configs.Suspension.rasstrelennayatyga2);
    }
}
