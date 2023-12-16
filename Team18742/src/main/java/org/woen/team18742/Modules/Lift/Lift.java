package org.woen.team18742.Modules.Lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;

public class Lift {
    private final DcMotor _liftM1;

    private final DigitalChannel _ending1, _ending2;

    private boolean _ending1State = false, _ending2State = false;

    public Lift(BaseCollector collector) {
        _ending1 = Devices.Ending1;
        _ending2 = Devices.Ending2;

        _liftM1 = Devices.LiftMotor;
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _liftM1.setPower(0);

        _ending1.setMode(DigitalChannel.Mode.INPUT);
        _ending2.setMode(DigitalChannel.Mode.INPUT);
    }

    public void Update() {
        _ending1State = _ending1.getState();
        _ending2State = _ending2.getState();

        if(_targetPose == LiftPose.UP)
            if(!_ending1State)
                _liftM1.setPower(0.95);
            else
                _liftM1.setPower(0.15);

        if(_targetPose == LiftPose.DOWN)
            if(!_ending2State)
                _liftM1.setPower(0.007);
            else
                _liftM1.setPower(0);
    }

    public boolean isATarget() {
        return (_targetPose == LiftPose.UP && _ending1State) || (_targetPose == LiftPose.DOWN && _ending2State);
    }

    public boolean isDown(){
        return _ending2State;
    }
    public boolean isUp(){
        return _ending1State;
    }

    public void SetLiftPose(LiftPose pose) {
        _targetPose = pose;
    }

    public void Start() {

    }

    private LiftPose _targetPose = LiftPose.DOWN;
}