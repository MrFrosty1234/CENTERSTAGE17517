package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Lift.LiftPose;

public class Manual {
    private BaseCollector _collector;

    private boolean _xOld = false, _lift = false, _clampOld = false, _plane = false, _AOld;
    private boolean recuiert = false;
    private boolean ferty = false;

    private Servo _servoPlane = null;

    private long _origmillis;

    public Manual(BaseCollector collector) {
        _collector = collector;
        _servoPlane = _collector.CommandCode.hardwareMap.get(Servo.class, "servoPlane");
    }

    public void Start() {
        _origmillis = System.currentTimeMillis();
    }

    boolean oldgrip;
    boolean oldperevprot;

    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);

        double LiftPlus = _collector.CommandCode.gamepad1.left_trigger;
        double LiftMinus = _collector.CommandCode.gamepad1.right_trigger;
        boolean A = _collector.CommandCode.gamepad1.square;
        boolean X = _collector.CommandCode.gamepad1.dpad_down;
        boolean O = _collector.CommandCode.gamepad1.dpad_up;
        boolean grip = _collector.CommandCode.gamepad1.triangle;
        boolean clamp = _collector.CommandCode.gamepad1.cross;
        boolean perevert = _collector.CommandCode.gamepad1.b;
        boolean zajat = _collector.CommandCode.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик

        if(grip && !oldgrip) {
            ferty = !ferty;
            _collector.Intake.setGripper(ferty);
        }

        oldgrip = grip;

        _collector.Lift.SetTargetPose(_collector.Lift.GetLiftPose() + LiftPlus * 10 - LiftMinus * 10);

        if (perevert && !oldperevprot)
            recuiert = !recuiert;
        _collector.Intake.setperevorotik(recuiert);
        oldperevprot = perevert;

        if (A && !_AOld && (zajat || _collector.Time.milliseconds() - _origmillis > 90000)) {
            if (_plane) {
                _servoPlane.setPosition(0.1);
                _plane = false;
            } else {
                _servoPlane.setPosition(0.6);
                _plane = true;
            }
        }

        if (X && _xOld) {
            _lift = !_lift;

            _collector.Lift.SetLiftPose(_lift ? LiftPose.DOWN : LiftPose.UP);
        }

        _xOld = X;

        if (O)
            _collector.Lift.SetLiftPose(LiftPose.ZEROING);

        if (clamp && _clampOld)
            _collector.Intake.SetGrabber();

        _clampOld = clamp;
    }
}