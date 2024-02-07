package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class EncoderControl {
    private final DcMotorEx _encoder;
    private final VelocityControl _velControl;
    private final double _ticks, _diameter;
    private double _startPos = 0, _pos;

    public EncoderControl(DcMotorEx encoder, ReductorType type, double diameter){
        this(encoder, type.Ticks, diameter);
    }

    public EncoderControl(DcMotorEx encoder, double ticksInEncoder, double diameter){
        _encoder = encoder;
        _velControl = new VelocityControl(encoder);

        _diameter = diameter;

        _ticks = ticksInEncoder;
    }

    public double GetPosition(){
        return _pos / _ticks * Math.PI * _diameter;
    }

    public double GetVelocity(){
        return _velControl.GetVelocity() / _ticks * Math.PI * _diameter;
    }

    public void Start(){
        _velControl.Start();
    }

    public void Update(){
        _velControl.Update();
        _pos = _encoder.getCurrentPosition() - _startPos;
    }

    public void Reset(){
        _startPos = _encoder.getCurrentPosition();
    }
}
