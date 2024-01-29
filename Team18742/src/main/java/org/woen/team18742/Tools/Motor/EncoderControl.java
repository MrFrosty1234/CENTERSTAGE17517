package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class EncoderControl {
    private final DcMotorEx _encoder;
    private final VelocityControl _velControl;
    private final double _ticks, _diameter;

    public EncoderControl(DcMotorEx encoder, ReductorType type, double diameter){
        this(encoder, type.Ticks, diameter);
    }

    public EncoderControl(DcMotorEx encoder, double ticksInEncoder, double diameter){
        _encoder = encoder;
        _velControl = new VelocityControl(encoder);

        _diameter = diameter;

        _ticks = ticksInEncoder;
    }

    public EncoderControl(Motor encoder, ReductorType type, double diameter){
        this(encoder.Motor, type.Ticks, diameter);
    }

    public EncoderControl(Motor encoder, double ticksInEncoder, double diameter){
        this(encoder.Motor, ticksInEncoder, diameter);
    }

    public double GetPosition(){
        return _encoder.getCurrentPosition() / _ticks * Math.PI * _diameter;
    }

    public double GetVelocity(){
        return _velControl.GetVelocity() / _ticks * Math.PI * _diameter;
    }

    public void Start(){
        _velControl.Start();
    }

    public void Update(){
        _velControl.Update();
    }

    public void Reset(){
        _encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
