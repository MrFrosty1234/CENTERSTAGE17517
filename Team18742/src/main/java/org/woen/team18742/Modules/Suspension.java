package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Brush.StaksBrush;
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
    private StaksBrush _stackBrush;
    private Brush _brush;
    private Lighting _lighting;
    private boolean _isRastrel = false;
    private final ElapsedTime _endGameTime = new ElapsedTime();

    @Override
    public void Start() {
        _endGameTime.reset();
        _isRastrel = false;
    }

    @Override
    public void Init(BaseCollector collector) {
        podtyaga1 = Devices.podtyaga1;
        podtyaga2 = Devices.podtyaga2;
        _podtyaga1 = Devices.Podtyagamotor;

        _stackBrush = collector.GetModule(StaksBrush.class);
        _brush = collector.GetModule(Brush.class);
        _lighting = collector.GetModule(Lighting.class);
    }

    public void Active() {
        if(_endGameTime.seconds() < 90)
            return;

        for(ServoImplEx i : Devices.Servs)
            i.setPwmDisable();

        podtyaga1.setPosition(Configs.Suspension.nulevayapodtyaga1);
        podtyaga2.setPosition(Configs.Suspension.nulevayapodtyaga2);

        _lighting.Disable = true;

        _isRastrel = true;
    }

    public void Disable() {
        podtyaga1.setPosition(Configs.Suspension.rasstrelennayatyga1);
        podtyaga2.setPosition(Configs.Suspension.rasstrelennayatyga2);
    }

    private Timer _timer = new Timer();

    public void unmotor() {
        if (_isRastrel) {
            _podtyaga1.setPower(1);
            _brush.BrushEnable();
            _stackBrush.servoSetDownPose();
        }
        else
            _podtyaga1.setPower(0);
        //_timer.Start(12000, ()->{
        //  _podtyaga1.setPower(0.0);
        //  });

    }
    public void motor() {
        _podtyaga1.setPower(0);
    }

    public void cbros()
    {
        _podtyaga1.setPower(-1);
    }
}
