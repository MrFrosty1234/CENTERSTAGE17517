package org.woen.team18742.Modules.Lift;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.PID;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Lift {
    private final DcMotor _liftMotor;

    private final DigitalChannel _ending1, _ending2;

    private boolean _endingUpState = false, _endingDownState = false;

    public static double PCoef = 0.1, ICoef = 0, DCoef = 0.1;

    private final PID _liftPid = new PID(PCoef, ICoef, DCoef, 1, 1);
    private BaseCollector _collector;

    public Lift(BaseCollector collector) {
        _collector = collector;

        _ending1 = Devices.Ending1;
        _ending2 = Devices.Ending2;

        _liftMotor = Devices.LiftMotor;
        _liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _liftMotor.setPower(0);

        ResetLift();

        _ending1.setMode(DigitalChannel.Mode.INPUT);
        _ending2.setMode(DigitalChannel.Mode.INPUT);
    }

    private void ResetLift(){
        _liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void Update() {
        _liftPid.UpdateCoefs(PCoef, ICoef, DCoef);

        _endingUpState = _ending1.getState();
        _endingDownState = _ending2.getState();

        if(!_collector.Intake.IsCoupNormal() && _liftPose == LiftPose.DOWN)
            _liftMotor.setPower(Math.max(_liftPid.Update(LiftPose.AVERAGE.Pose - _liftMotor.getCurrentPosition()) / Battery.ChargeDelta, 0.007));
        else
            _liftMotor.setPower(Math.max(_liftPid.Update(_liftPose.Pose - _liftMotor.getCurrentPosition()) / Battery.ChargeDelta, 0.007));

        ToolTelemetry.AddLine("Lift end1 = " + _endingUpState + " end = " + _endingDownState);

        if(_endingDownState)
            ResetLift();
    }

    public boolean isATarget() {
        return Math.abs(_liftPid.Err) < 30;
        //return (_liftPose == LiftPose.UP && _endingUpState) || (_liftPose == LiftPose.DOWN && _endingDownState) || (_liftPose == LiftPose.AVERAGE && Math.abs(_liftPid.Err) < 30);
    }

    public boolean isDown(){
        return _liftPose == LiftPose.DOWN && isATarget();
    }
    public boolean isUp(){
        return _liftPose == LiftPose.UP && isATarget();
    }
    public boolean isAverage(){return _liftPose == LiftPose.AVERAGE && isATarget();}

    public void SetLiftPose(LiftPose pose) {
        _liftPose = pose;
    }

    public void Start() {
        _liftPid.Start();
    }

    private LiftPose _liftPose = LiftPose.DOWN;
}