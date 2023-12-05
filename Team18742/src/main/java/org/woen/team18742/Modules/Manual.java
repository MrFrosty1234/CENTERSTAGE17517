package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Lift.LiftPose;

public class Manual {
    private BaseCollector _collector;

    private boolean _xOld = false, _lift = false;
    private boolean recuiert = false;

    private Servo _servoPlane = null;

    private long _origmillis;

    public Manual(BaseCollector collector) {
        _collector = collector;
        _servoPlane = _collector.CommandCode.hardwareMap.get(Servo.class, "servoPlane");
    }

    public void Start(){
         _origmillis = System.currentTimeMillis();
    }

    boolean oldgrip;
    boolean oldperevprot;
    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);

        boolean A = _collector.CommandCode.gamepad1.square;
        boolean X = _collector.CommandCode.gamepad1.dpad_down;
        boolean O = _collector.CommandCode.gamepad1.dpad_up;
        boolean grip = _collector.CommandCode.gamepad1.triangle;
        boolean clamp = _collector.CommandCode.gamepad1.cross;
        boolean perevert = _collector.CommandCode.gamepad1.b;
        boolean zajat =_collector.CommandCode.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик

       _collector.Intake.setGripper(grip);

        if(grip && !oldgrip)
            _collector.Intake.setGripper(grip);
        oldgrip = grip;


        if(perevert && !oldperevprot)
            recuiert = !recuiert;
        _collector.Intake.setperevorotik(recuiert);
        oldperevprot = perevert;
        if (A && (zajat || _collector.Time.milliseconds() - _origmillis > 90000))
            _servoPlane.setPosition(0.50);
        else
            _servoPlane.setPosition(0.83);

        if(X && _xOld) {
            _lift = !_lift;

            _collector.Lift.SetLiftPose(_lift ? LiftPose.DOWN : LiftPose.UP);
        }

        _xOld = X;

        if(O)
            _collector.Lift.SetLiftPose(LiftPose.ZEROING);
    }
}