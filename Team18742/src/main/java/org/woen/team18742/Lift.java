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

    private PID liftPid;

    private double _targetLiftPose = 0;

    private BaseCollector _collector;
    double origmillis = System.currentTimeMillis();
    private boolean _Lift = false, _XOld = false;
    private boolean _Lift1 = false, _YOld = false;
    private boolean _Lift2 = false, _OOld = false;
    private boolean _Liftchetka = false, _oldChetka = false;

    public Lift(BaseCollector collector) {
        _collector = collector;

        _grabberDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "grabbermotor");
        _liftM1 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor1");
        _liftM2 = _collector.CommandCode.hardwareMap.get(DcMotor.class, "liftmotor2");
        _liftM1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _liftM2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _servoLift1 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift1");
        _servoLift2 = _collector.CommandCode.hardwareMap.get(Servo.class, "servoLift2");
        _servoPlane = _collector.CommandCode.hardwareMap.get(Servo.class, "servoPlane");

        liftPid = new PID(1, 1, 1, 1);

        _grabberDrive.setPower(0);
        _liftM1.setPower(0);
        _liftM2.setPower(0);
        _liftM1.setDirection(REVERSE);

        _servoLift1.setPosition(0);
        _servoLift2.setPosition(0);
    }

    public void Update() {
        boolean X = _collector.CommandCode.gamepad1.left_bumper;
        boolean Y = _collector.CommandCode.gamepad1.triangle;
        boolean O = _collector.CommandCode.gamepad1.circle;
        boolean A = _collector.CommandCode.gamepad1.square;
        boolean chetka = _collector.CommandCode.gamepad1.cross;

        if (X && !_XOld)
            _Lift = !_Lift;

        _XOld = X;

        if (A && System.currentTimeMillis() - origmillis > 90000)
            _servoPlane.setPosition(0.50);
        else
            _servoPlane.setPosition(0.83);
        ////////////////////////////////

        //////////////////////////////

        //////////////////////////////
        if (O && !_OOld)
            _Lift2 = !_Lift2;
        _OOld = O;


        ////////////////////////////////

        if (chetka && !_oldChetka) {
            _Liftchetka = !_Liftchetka;
        }
        _oldChetka = chetka;

        if (_Liftchetka) {
            _grabberDrive.setPower(-1);
        } else if (_Lift2) {
            _grabberDrive.setPower(1);
        } else {
            _grabberDrive.setPower(0);
        }

        ////////////////////////////////
    }

    public void Start() {
       // _grabberDrive.setPower(1);

        _liftM1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _liftM1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}