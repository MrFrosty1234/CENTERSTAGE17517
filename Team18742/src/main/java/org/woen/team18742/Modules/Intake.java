package org.woen.team18742.Modules;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Intake implements IRobotModule {
    private Servo servoTurn;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private DcMotor _lighting;
private Brush _brush;
    private Lift _lift;

    @Override
    public void Init(BaseCollector collector) {
        pixelSensor1 = Devices.PixelSensor1;
        pixelSensor2 = Devices.PixelSensor2;
        gripper = Devices.Gripper;
        clamp = Devices.Clamp;
        servoTurn = Devices.Servopere;
        _lighting = Devices.LightingMotor;

        _lift = collector.GetModule(Lift.class);
        _brush = collector.GetModule(Brush.class);
    }

    public void updateTurner() {
        if (_lift.isUp()) {
            servoTurn.setPosition(Configs.Intake.servoTurnTurned);
        } else if (_lift.isAverage()) {
            servoTurn.setPosition(Configs.Intake.servoTurnTurned);
            _normalTurnTimer.reset();
        } else {
            servoTurn.setPosition(Configs.Intake.servoTurnNormal);
        }
    }

    public boolean IsTurnNormal() {
        return _normalTurnTimer.milliseconds() > Configs.Intake.AverageTime;
    }

    private ElapsedTime _normalTurnTimer = new ElapsedTime(Configs.Intake.AverageTime);

    private boolean _pixelGripped = false;

    public void setGripper(boolean grip) {
        if (grip) {
            gripper.setPosition(Configs.Intake.servoGripperGripped);
        } else {
            gripper.setPosition(Configs.Intake.servoGripperNormal);
        }
        _pixelGripped = grip;

        _lighting.setPower(grip ? 1 : 0);
    }

    public boolean isPixelGripped() {
        return _pixelGripped;
    }


    public void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(Configs.Intake.servoClampClamped);
        } else {
            clamp.setPosition(Configs.Intake.servoClampReleased);
        }
    }


    ElapsedTime pixelTimer = new ElapsedTime();

    private boolean isPixelDetected() {
        if (pixelSensor2.getVoltage() >= Configs.Intake.pixelSensorvoltage || !_brush.isBrusnOn()/*&& pixelSensor2.getVoltage() >= pixelSensorvoltage*/)
            pixelTimer.reset();
        return pixelTimer.milliseconds() > Configs.Intake.pixelDetectTimeMs;
    }

    ElapsedTime _clampTimer = new ElapsedTime();
    double clampTimerconst = 800;

    void releaseGripper() {
        setGripper(false);
        _clampTimer.reset();
    }

    @Override
    public void Update() {
        if (isPixelDetected()) {
            setGripper(true);
            setClamp(_clampTimer.milliseconds() < clampTimerconst && _lift.isDown());
        } else {
            _clampTimer.reset();
            setClamp(!_pixelGripped && _lift.isDown());
        }

        updateTurner();

        ToolTelemetry.AddLine("Pixels:" + pixelSensor1.getVoltage() + "," + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine("Detected:" + isPixelDetected());
    }

    public void PixelCenterGrip(boolean gripped) {
        gripper.setPosition(gripped ? Configs.Intake.PixelCenterOpen : Configs.Intake.servoGripperNormal);
    }
}