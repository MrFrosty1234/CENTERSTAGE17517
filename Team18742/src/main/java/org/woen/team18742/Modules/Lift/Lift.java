package org.woen.team18742.Modules.Lift;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Lift implements IRobotModule {
    private DcMotor _liftMotor;

    private DigitalChannel _ending1, _ending2;

    private boolean _endingUpState = false, _endingDownState = false;

    private final PIDF _liftPIDF = new PIDF(Configs.LiftPid.PCoef, Configs.LiftPid.ICoef, Configs.LiftPid.DCoef, 0.1, 0, 1, 1);
    private Intake _intake;

    @Override
    public void Init(BaseCollector collector) {
        _intake = collector.GetModule(Intake.class);

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

    @Override
    public void Update() {
        _liftPIDF.UpdateCoefs(Configs.LiftPid.PCoef, Configs.LiftPid.ICoef, Configs.LiftPid.DCoef);

        _endingUpState = _ending1.getState();
        _endingDownState = _ending2.getState();

        if(!_intake.IsCoupNormal() && _liftPose == LiftPose.DOWN)
            _liftMotor.setPower(Math.max(_liftPIDF.Update(LiftPose.AVERAGE.Pose - _liftMotor.getCurrentPosition()) / Battery.ChargeDelta, 0.007));
        else
            _liftMotor.setPower(Math.max(_liftPIDF.Update(_liftPose.Pose - _liftMotor.getCurrentPosition()) / Battery.ChargeDelta, 0.007));

        ToolTelemetry.AddLine("Lift end1 = " + _endingUpState + " end = " + _endingDownState);

        if(_endingDownState)
            ResetLift();
    }

    @Override
    public void Stop() {}

    public boolean isATarget() {
        return Math.abs(_liftPIDF.Err) < 30;
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

    @Override
    public void Start() {
        _liftPIDF.Start();
    }

    private LiftPose _liftPose = LiftPose.DOWN;
}