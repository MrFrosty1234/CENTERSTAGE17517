package org.woen.team18742.Tools.Motor;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Tools.Configs.Configs;

public class VelocityControl {
    private final DcMotorEx _encoder;
    private final ElapsedTime _deltaTime = new ElapsedTime();

    private double _oldPosition = 0, _speed = 0;

    public double GetVelocity() {
        return _speed;
    }

    public VelocityControl(DcMotorEx encoder) {
        _encoder = encoder;
    }

    private double _mathSpeed;

    public void Update() {
        double encoderPosition = _encoder.getCurrentPosition();

        double hardwareSpeed = _encoder.getVelocity();

        if (_deltaTime.seconds() > 0.088) {
            _mathSpeed = (encoderPosition - _oldPosition) / _deltaTime.seconds();
            _deltaTime.reset();
            _oldPosition = encoderPosition;
        }

        _speed = hardwareSpeed + Math.round((_mathSpeed - hardwareSpeed) / (double) (1 << 16)) * (double)(1 << 16);
    }

    public void Start() {
        _deltaTime.reset();
        _oldPosition = _encoder.getCurrentPosition();
    }
}
