package org.woen.team18742.Modules.Intake;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
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
    private Servo gripper, _lineServo; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1; // Датчик присутствия пикселей над прижимом
    private Brush _brush;
    private Lift _lift;
    private BaseCollector _collector;

    @Override
    public void Init(BaseCollector collector) {
        _collector = collector;

        pixelSensor1 = Devices.PixelSensor;
        gripper = Devices.Gripper;
        clamp = Devices.Clamp;
        servoTurn = Devices.Servopere;
        _lineServo = Devices.LineServo;

        _lift = collector.GetModule(Lift.class);
        _brush = collector.GetModule(Brush.class);
    }

    private final Timer _normalTimer = new Timer();
    private boolean _oldTurnPos = false;

    private GripperStates _gripperState = GripperStates.OPEN;

    public void updateTurner() {
        if (_lift.isTurnPosPassed()) {
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

    @Override
    public void Start() {
        if(_collector instanceof AutonomCollector)
            LineServoClose();
        else
            LineServoOpen();
    }

    public boolean IsTurnNormal() {
        return _isTurned;
    }

    private boolean _isTurned = true;

    public boolean isPixelGripped() {
        return _gripperState != GripperStates.OPEN;
    }

    private void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(Configs.Intake.servoClampClamped);
        } else if(_lift.isDown()) {
            clamp.setPosition(Configs.Intake.servoClampReleased);
        }
        else{
            clamp.setPosition(Configs.Intake.servoClampReleasedLift);
        }
    }


    private ElapsedTime _pixelTimer = new ElapsedTime();

    private boolean isPixelDetected() {
        if (pixelSensor1.getVoltage() >= Configs.Intake.pixelSensorvoltage || !_brush.isBrusnOn())
            _pixelTimer.reset();
        return _pixelTimer.milliseconds() > Configs.Intake.pixelDetectTimeMs;
    }

    private ElapsedTime _clampTimer = new ElapsedTime();
    private double _clampTimerconst = 800;

    public void releaseGripper() {
        if(_lift.isDown() || !_lift.isATarget() || !isPixelGripped())
            return;

        if(_gripperState == GripperStates.ONE_GRIPPED) {
            _clampTimer.reset();

            _liftTimer.Start(400, () -> _lift.SetLiftPose(LiftPose.DOWN));

            _gripperState = GripperStates.OPEN;

            return;
        }

        _gripperState = GripperStates.ONE_GRIPPED;
    }

    public void releaseAllGripper(){
        releaseGripper();
        releaseGripper();
    }

    private final Timer _liftTimer = new Timer();

    @Override
    public void Update() {
        if (isPixelDetected() && _lift.isDown()) {
            _gripperState = GripperStates.ALL_GRIPPED;
            setClamp(_clampTimer.milliseconds() < _clampTimerconst);
        } else {
            _clampTimer.reset();
            setClamp(!isPixelGripped() && _lift.isDown());
        }

        gripper.setPosition(_gripperState.getServoPosition());

        updateTurner();

        ToolTelemetry.AddLine("Pixels:" + pixelSensor1.getVoltage());
    }

    public void LineServoOpen(){
        _lineServo.setPosition(Configs.Intake.LineServoOpen);
    }

    public void LineServoClose(){
        _lineServo.setPosition(Configs.Intake.LineServoClose);
    }
}