package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Lift {
    private DcMotor _grabberDrive = null;
    private DcMotor _liftM1 = null;
    private DcMotor _liftM2 = null;

    void moveLift(double distance) {
        _collector.Driver.ResetIncoder();
        double errold;
        double kp = 1;
        double kd = 1;
        double err = distance - _collector.Driver.GetDistance();
        errold = err;
        while (_collector.CommandCode.opModeIsActive() && abs(err) > 2) {
            err = distance - _collector.Driver.GetDistance();
            double u = (err * kp) + (err - errold) * kd;
            _liftM1.setPower(u);
            _liftM2.setPower(u);
        }
        _liftM1.setPower(0);
        _liftM2.setPower(0);
    }

    private Collector _collector;

    private boolean _Lift = false, _XOld = false;

    public Lift(Collector collector) {
        _collector = collector;

        _grabberDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "grabbermotor");
        _liftM1 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _liftM2 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor2");
        _grabberDrive.setPower(0);
        _liftM1.setPower(0);
        _liftM2.setPower(0);
        _liftM1.setDirection(REVERSE);
    }

    public void Update() {
        boolean X = _collector.CommandCode.gamepad1.x;

        if (X && !_XOld)
            _Lift = !_Lift;

        _XOld = X;

        if (_Lift)
            _grabberDrive.setPower(-1);
    }
}
