package org.woen.team18742;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    private DcMotor _liftM1 = null;
    public Servo _servoLift1, _servoLift2;

    private PID _liftPid;

    private double _targetLiftPose = 0;

    private BaseCollector _collector;

    private DigitalChannel _ending;

    public Lift(BaseCollector collector) {
        _collector = collector;

        _liftM1 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _ending = _collector.CommandCode.hardwareMap.get(DigitalChannel.class,"ending");
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _servoLift1 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift1");
        _servoLift2 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift2");

        _liftPid = new PID(1, 1, 1, 1);

        _liftM1.setPower(0);

        _ending.setMode(DigitalChannel.Mode.INPUT);

        _servoLift1.setPosition(0);
        _servoLift2.setPosition(0);
    }

    public void Update() {
        _liftM1.setPower(_liftPid.Update(_targetLiftPose - _liftM1.getCurrentPosition()));
    }

    public boolean isATarget(){
        return Math.abs(_liftPid.Err) < 2;
    }

    public void SetLiftPose(double pos){
        _targetLiftPose = pos;
    }

    public void Start() {
        _liftM1.setPower(-0.3);

        while (!_ending.getState());

        _liftM1.setPower(0);

        _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}