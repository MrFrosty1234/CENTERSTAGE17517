package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    private DcMotor _grabberDrive = null;
    private DcMotor _liftM1 = null;
    private DcMotor _liftM2 = null;
    private Servo _servoLift1, _servoLift2;

    void moveLift(double distance) {
        new Thread() {
            @Override
            public void run() {
                _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                double errold;
                double kp = 1;
                double kd = 1;
                double err = distance - _liftM1.getCurrentPosition() / 480;
                errold = err;
                while (abs(err) > 2 && _collector.CommandCode.opModeIsActive()) {
                    err = distance - _liftM1.getCurrentPosition() / 480;
                    double u = (err * kp) + (err - errold) * kd;
                    _liftM1.setPower(u);
                    _liftM2.setPower(u);
                }
                _liftM1.setPower(0);
                _liftM2.setPower(0);
            }
        }.start();
    }

    private Collector _collector;

    private boolean _Lift = false, _XOld = false;

    public Lift(Collector collector) {
        _collector = collector;

        _grabberDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "grabbermotor");
        _liftM1 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _liftM2 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor2");

        _servoLift1 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift1");
        _servoLift2 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift2");

        _grabberDrive.setPower(0);
        _liftM1.setPower(0);
        _liftM2.setPower(0);
        _liftM1.setDirection(REVERSE);

        _servoLift1.setPosition(0);
        _servoLift2.setPosition(0);
    }

    public void Update() {
        boolean X = _collector.CommandCode.gamepad1.x;

        if (X && !_XOld)
            _Lift = !_Lift;

        _XOld = X;

        if (_Lift) {
            moveLift(51);
            _servoLift1.setPosition(0);
            _servoLift2.setPosition(0);
        }
        else {
            moveLift(0);
            _servoLift1.setPosition(0);
            _servoLift2.setPosition(0);
        }
    }

    public void Start() {
        _grabberDrive.setPower(1);
    }
}