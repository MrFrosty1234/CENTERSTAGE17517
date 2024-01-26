package org.woen.team18742.Tools.Motor;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Tools.Configs.Configs;

public class VelocityControl {
    private final DcMotorEx _encoder;
    private final ElapsedTime _deltaTime = new ElapsedTime();

    private double _oldPosition = 0, _speed = 0;

    public double GetSpeed(){
        return _speed;
    }

    public VelocityControl(DcMotorEx encoder){
        _encoder = encoder;
    }

    public void Update(){
        double encoderPosition = _encoder.getCurrentPosition();

        double hardwareSpeed = _encoder.getVelocity(),
                mathSpeed = (encoderPosition - _oldPosition) / _deltaTime.seconds();

        _oldPosition = encoderPosition;

        _speed = hardwareSpeed + Math.round(mathSpeed - hardwareSpeed);

        _deltaTime.reset();
    }

    public void Start(){
        _deltaTime.reset();
    }
}
