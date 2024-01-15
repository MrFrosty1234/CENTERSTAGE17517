package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team18742.Tools.PIDF;

public class Motor {
    private final DcMotorEx _motor;

    private PIDF _velocityPid;

    private final ReductorType EncoderType;

    public Motor(DcMotorEx motor, ReductorType type, PIDF pid){
        this(motor, type);

        _velocityPid = pid;
    }

    public Motor(DcMotorEx motor, ReductorType type){
        _motor = motor;

        EncoderType = type;

        MotorsHandler.AddMotor(this);
    }

    public void Start(){

    }

    public void Update(){

    }
}
