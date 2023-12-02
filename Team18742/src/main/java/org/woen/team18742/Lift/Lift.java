package org.woen.team18742.Lift;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.PID;

@Config
public class Lift {
    public static double PidCoefP = 1, PidCoefI = 1, PidCoefD = 1;

    private final DcMotor _liftM1;

    private final PID _liftPid;

    private double _targetLiftPose = 0;

    private final DigitalChannel _ending;

    public Lift(BaseCollector collector) {
        _ending = collector.CommandCode.hardwareMap.get(DigitalChannel.class,"ending");
        _liftM1 = collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _liftPid = new PID(PidCoefP, PidCoefI, PidCoefD, 1);

        _liftM1.setPower(0);

        _ending.setMode(DigitalChannel.Mode.INPUT);
    }

    public void Update() {
        _liftPid.UpdateCoefs(PidCoefP, PidCoefI, PidCoefD);

        if (!_ending.getState()) {
            _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        _liftM1.setPower(_liftPid.Update(_targetLiftPose - _liftM1.getCurrentPosition()));
    }

    public boolean isATarget() {
        return Math.abs(_liftPid.Err) < 2;
    }

    public void SetLiftPose(LiftPose pose) {
        _targetLiftPose = pose.Pose;
    }

    public void Start() {
        SetLiftPose(LiftPose.ZEROING);
    }
}