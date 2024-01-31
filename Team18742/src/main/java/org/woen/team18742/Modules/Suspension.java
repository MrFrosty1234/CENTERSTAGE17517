package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.Timers.Timer;

@Module
public class Suspension implements IRobotModule {
    private Servo podtyaga1;
    private Servo podtyaga2;
    private DcMotorEx _podtyaga1;
    private ElapsedTime RevTime1 = new ElapsedTime();

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

    private Timer _timer = new Timer();

    public void unmotor() {
        _podtyaga1.setPower(0);

        _timer.Start(4000, ()->{
            _podtyaga1.setPower(0);
        });
    }
}
