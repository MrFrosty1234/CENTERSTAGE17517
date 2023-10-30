package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    private DcMotor _grabberDrive = null;
    private DcMotor _liftM1 = null;
    private DcMotor _liftM2 = null;
    public Servo _servoLift1, _servoLift2, _servoPlane;

    private PID _liftPid1, _liftPid2;

    private double _targetLiftPose = 0;

    private CollectorSample _collector;

    private boolean _Lift = false, _XOld = false;
    private boolean _Lift1 = false, _YOld = false;
    private boolean _Lift2 = false, _OOld = false;

    public Lift(CollectorSample collector) {
        _collector = collector;

        _grabberDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "grabbermotor");
        _liftM1 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _liftM2 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor2");
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _liftM2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _servoLift1 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift1");
        _servoLift2 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift2");
        _servoPlane = _collector.CommandCode.hardwareMap.get(Servo.class, "servoPlane");

        _liftPid1 = new PID(1, 1, 1, 1);
        _liftPid2 = new PID(1, 1, 1, 1);

        _grabberDrive.setPower(0);
        _liftM1.setPower(0);
        _liftM2.setPower(0);
        _liftM1.setDirection(REVERSE);

        _servoLift1.setPosition(0);
        _servoLift2.setPosition(0);
        _servoPlane.setPosition(0);
    }

    public void Update() {
        boolean X = _collector.CommandCode.gamepad1.cross;
        boolean Y = _collector.CommandCode.gamepad1.triangle;
        boolean O = _collector.CommandCode.gamepad1.circle;
        boolean A = _collector.CommandCode.gamepad1.square;

        if (X && !_XOld)
            _Lift = !_Lift;

        _XOld = X;

        if (A && System.currentTimeMillis() > 90000)
            _servoPlane.setPosition(0);

        if (_Lift)
            _targetLiftPose = 51;
        else
            _targetLiftPose = 0;

        _liftM1.setPower(_liftPid1.Update(_liftM1.getCurrentPosition() / 480, _targetLiftPose));
        _liftM2.setPower(_liftPid2.Update(_liftM2.getCurrentPosition() / 480, _targetLiftPose));

        if (Y && !_YOld)
            _Lift1 = !_Lift1;

        _YOld = Y;

        if (_Lift1) {
            _servoLift1.setPosition(0);
        } else {
            _servoLift1.setPosition(0);
        }

        if (O && !_OOld)
            _Lift2 = !_Lift2;

        _OOld = X;

        if (_Lift2) {
            _servoLift2.setPosition(0);
        } else {
            _servoLift2.setPosition(0);
        }

        if (O && !_OOld)
            _Lift2 = !_Lift2;

        _OOld = X;

        if (_Lift2) {
            _servoLift2.setPosition(0);
        } else {
            _servoLift2.setPosition(0);
        }
    }

    public void Start() {
        _grabberDrive.setPower(1);

        _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _liftM2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}