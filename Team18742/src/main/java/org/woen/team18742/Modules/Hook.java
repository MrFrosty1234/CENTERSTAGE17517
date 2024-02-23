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
public class Hook implements IRobotModule {
    private Servo _hookServoLeft;
    private Servo _hookServoRight;
    private DcMotorEx _hookMotor;
    private Lighting _lighting;
    private boolean _isOpen = false;
    private final ElapsedTime _endGameTime = new ElapsedTime();

    @Override
    public void Start() {
        _endGameTime.reset();
        _isOpen = false;

        _hookServoLeft.setPosition(Configs.Hook.ServoHookClosedLeft);
        _hookServoRight.setPosition(Configs.Hook.ServoHookClosedRight);
    }

    @Override
    public void Init(BaseCollector collector) {
        _hookServoLeft = Devices.HookServoLeft;
        _hookServoRight = Devices.HookServoRight;
        _hookMotor = Devices.HookMotor;

        _lighting = collector.GetModule(Lighting.class);
    }

    private Timer _hookTimer = new Timer();

    public void Active(boolean timerBypass) {
        if((_endGameTime.seconds() < 90 && !timerBypass) || _isOpen)
            return;

        _hookServoLeft.setPosition(Configs.Hook.ServoHookOpenLeft);
        _hookServoRight.setPosition(Configs.Hook.ServoHookOpenRight);

        _hookTimer.Start(800, ()->{
            _lighting.Disable();

            for(ServoImplEx i : Devices.Servs)
                i.setPwmDisable();
        });

        _isOpen = true;
    }


    public void hookUp() {
        if (_isOpen)
            _hookMotor.setPower(-1);
        else
            Stop();
    }

    public void hookDown()
    {
        if (_isOpen)
            _hookMotor.setPower(1);
        else
            Stop();
    }

    @Override
    public void Stop(){
        _hookMotor.setPower(0);
    }
}
