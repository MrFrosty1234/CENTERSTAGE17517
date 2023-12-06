package org.woen.team18742.Lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.PID;

public class Lift {
    private final DcMotor _liftM1;

    private final PID _liftPid;

    private double _targetLiftPose = 0;

    private final DigitalChannel _ending;
    private ElapsedTime _time;

    public Lift(BaseCollector collector) {
        _ending = collector.CommandCode.hardwareMap.get(DigitalChannel.class,"ending");
        _liftM1 = collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor");
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _liftPid = new PID(.001, 0, 0, 1);

      //  _liftM1.setPower(0);

        _ending.setMode(DigitalChannel.Mode.INPUT);


    }

    public void Update() {
        boolean state = !_ending.getState();

        if (state) {
            _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (_isZeroing) {
            if (state || _time.milliseconds() - _zeroingTimeStart < 5000) {
                _isZeroing = false;
           //     _liftM1.setPower(0);
            }
        } else {
            // _liftM1.setPower(_liftPid.Update(_targetLiftPose - _liftM1.getCurrentPosition()));
        }
    }

    public boolean isATarget() {
        return Math.abs(_liftPid.Err) < 2;
    }

    public void SetLiftPose(LiftPose pose) {
        _targetLiftPose = pose.Pose;
    }

    public void Start() {
        //Zeroing();
    }

    private boolean _isZeroing = false;

    private long _zeroingTimeStart;

    public void Zeroing() {
      //  _liftM1.setPower(-0.3);

        _zeroingTimeStart = System.currentTimeMillis();

        _isZeroing = true;
    }
}