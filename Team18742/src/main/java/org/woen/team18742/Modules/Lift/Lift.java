package org.woen.team18742.Modules.Lift;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.PID;

@Config
public class Lift {
    private final DcMotor _liftM1;

    private final DigitalChannel _ending1, _ending2;

    private boolean _ending1State = false, _ending2State = false;

    public static double PCoef = 0.01, ICoef = 0.1, DCoef = 0, gravityCoef = 0.01;

    private PID _liftPid = new PID(PCoef, ICoef, DCoef, 1, 1);
    private double _targetPoseDouble = 0;

    public Lift(BaseCollector collector) {
        _ending1 = Devices.Ending1;
        _ending2 = Devices.Ending2;

        _liftM1 = Devices.LiftMotor;
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _liftM1.setPower(0);

        ResetLift();

        _ending1.setMode(DigitalChannel.Mode.INPUT);
        _ending2.setMode(DigitalChannel.Mode.INPUT);
    }

    private void ResetLift(){
        _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void Update() {
        _liftPid.UpdateCoefs(PCoef, ICoef, DCoef);

        _ending1State = _ending1.getState();
        _ending2State = _ending2.getState();

        double u =_liftPid.Update(_targetPoseDouble - _liftM1.getCurrentPosition()) + gravityCoef;

        if(u < 0.007)
            _liftM1.setPower(0.007);
        else
            _liftM1.setPower(u);

        if(_ending2State)
            ResetLift();
    }

    public boolean isATarget() {
        return Math.abs(_liftPid.Err) < 10;
    }

    public boolean isDown(){
        return _liftPose == LiftPose.DOWN && isATarget();
    }
    public boolean isUp(){
        return _liftPose == LiftPose.UP && isATarget();
    }
    public boolean isAverage(){return  _liftPose == LiftPose.AVERAGE && isATarget();}
    public boolean isMoveAverage(){
        if(_liftPid.Err > 0)
            return _liftM1.getCurrentPosition() + 10 > LiftPose.AVERAGE.Pose;

        return _liftM1.getCurrentPosition() - 10 > LiftPose.AVERAGE.Pose;
    }

    public void SetLiftPose(LiftPose pose) {
        _targetPoseDouble = pose.Pose;
        _liftPose = pose;
    }

    public void Start() {

    }

    private LiftPose _liftPose = LiftPose.DOWN;
}