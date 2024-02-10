package org.woen.team18742.Modules;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.Timers.Timer;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Intake implements IRobotModule {
    private Servo servoTurn;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1; // Датчик присутствия пикселей над прижимом
    private Brush _brush;
    private Lift _lift;

    @Override
    public void Init(BaseCollector collector) {
        pixelSensor1 = Devices.PixelSensor;
        gripper = Devices.Gripper;
        clamp = Devices.Clamp;
        servoTurn = Devices.Servopere;

        _lift = collector.GetModule(Lift.class);
        _brush = collector.GetModule(Brush.class);
    }

    private final Timer _normalTimer = new Timer();
    private boolean _oldTurnPos = false;

    public void updateTurner() {
        if (_lift.isProchelnugnoepologenie()) {
            servoTurn.setPosition(Configs.Intake.servoTurnTurned);

            _isTurned = false;

            _oldTurnPos = true;
        } else {
            servoTurn.setPosition(Configs.Intake.servoTurnNormal);

            if (_oldTurnPos) {
                _normalTimer.Start(500, () -> {
                    _isTurned = true;
                });
            }

            _oldTurnPos = false;
        }
    }

    public boolean IsTurnNormal() {
        return _isTurned;
    }

    private boolean _isTurned = true;

    private boolean _pixelGripped = false;

    public void setGripper(boolean grip) {
        if (grip) {
            gripper.setPosition(Configs.Intake.servoGripperGripped);
        } else {
            gripper.setPosition(Configs.Intake.servoGripperNormal);
        }

        _pixelGripped = grip;
    }

    public boolean isPixelGripped() {
        return _pixelGripped;
    }

    public void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(Configs.Intake.servoClampClamped);
        } else if(_lift.isDown()) {
            clamp.setPosition(Configs.Intake.servoClampReleased);
        }
        else{
            clamp.setPosition(Configs.Intake.servoClampReleasedLift);
        }
    }


    ElapsedTime pixelTimer = new ElapsedTime();

    private boolean isPixelDetected() {
        if (pixelSensor1.getVoltage() >= Configs.Intake.pixelSensorvoltage || !_brush.isBrusnOn())
            pixelTimer.reset();
        return pixelTimer.milliseconds() > Configs.Intake.pixelDetectTimeMs;
    }

    ElapsedTime _clampTimer = new ElapsedTime();
    double clampTimerconst = 800;

    public void releaseGripper() {
        setGripper(false);
        _clampTimer.reset();

        _liftTimer.Start(400, () -> _lift.SetLiftPose(LiftPose.DOWN));
    }

    private final Timer _liftTimer = new Timer();

    @Override
    public void Update() {
        if (isPixelDetected() && _lift.isDown()) {
            setGripper(true);
            setClamp(_clampTimer.milliseconds() < clampTimerconst && _lift.isDown());
        } else {
            _clampTimer.reset();
            setClamp(!_pixelGripped && _lift.isDown());
        }

        updateTurner();

        ToolTelemetry.AddLine("Pixels:" + pixelSensor1.getVoltage());
        ToolTelemetry.AddLine("Detected:" + isPixelDetected());
    }

    /*public void PixelCenterGrip(boolean gripped) {
        gripper.setPosition(gripped ? Configs.Intake.PixelCenterOpen : Configs.Intake.servoGripperNormal);

        _pixelGripped = gripped;
    }*/
}