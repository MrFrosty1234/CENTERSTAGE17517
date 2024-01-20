package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.ToolTelemetry;

public class Motor {
    private final DcMotorEx _motor;

    private PIDF _velocityPid;

    private final ReductorType _encoderType;

    private boolean _isCustomPid = false;

    public Motor(DcMotorEx motor, ReductorType type, PIDF pid){
        this(motor, type);

        _velocityPid = pid;

        _isCustomPid = true;
    }

    public Motor(DcMotorEx motor, ReductorType type){
        _motor = motor;

        _encoderType = type;

        _velocityPid = new PIDF(Configs.Motors.DefultP, Configs.Motors.DefultI, Configs.Motors.DefultD, 0, Configs.Motors.DefultF, 1, 1);

        MotorsHandler.AddMotor(this);
    }

    public void Start(){
        _velocityPid.Start();
    }

    public void setDirection(DcMotorSimple.Direction dir){
        _motor.setDirection(dir);
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior power){
        _motor.setZeroPowerBehavior(power);
    }

    public void setMode(DcMotor.RunMode mode){
        _motor.setMode(mode);
    }

    public double getCurrentPosition(){
        return _motor.getCurrentPosition();
    }

    public void Update(){
        if(!_isCustomPid)
            _velocityPid.UpdateCoefs(Configs.Motors.DefultP, Configs.Motors.DefultI, Configs.Motors.DefultD, 0, Configs.Motors.DefultF);

        double voltageSpeed = _targetVoltageSpeed / Battery.Voltage;
        double pidSpeed = _velocityPid.Update(_targetEncoderSpeed - _motor.getVelocity(), _targetEncoderSpeed);

        _motor.setPower((voltageSpeed + pidSpeed) / 2);
    }

    private double _targetEncoderSpeed = 0, _targetVoltageSpeed = 0;

    public void setPower(double speed){
        _targetEncoderSpeed = speed * _encoderType.Ticks;
        _targetVoltageSpeed = Configs.Battery.CorrectCharge * speed;
    }

    public void setEncoderPower(double speed){
        _targetEncoderSpeed = speed;
        _targetVoltageSpeed = Configs.Battery.CorrectCharge * (speed / _encoderType.Ticks);
    }
}
