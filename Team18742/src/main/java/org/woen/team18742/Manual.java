package org.woen.team18742;

import com.qualcomm.robotcore.hardware.Servo;

public class Manual {
    private BaseCollector _collector;

    private boolean _xOld = false, _lift = false;

    private Servo _servoPlane = null;

    private long _origmillis = System.currentTimeMillis();

    public Manual(BaseCollector collector) {
        _collector = collector;
        _servoPlane = _collector.CommandCode.hardwareMap.get(Servo.class, "servoPlane");
    }

    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);

        boolean A = _collector.CommandCode.gamepad1.square;
        boolean X = _collector.CommandCode.gamepad1.cross;

        if (A && System.currentTimeMillis() - _origmillis > 90000)
            _servoPlane.setPosition(0.50);
        else
            _servoPlane.setPosition(0.83);

        if(X && _xOld) {
            _lift = !_lift;

            _collector.Lift.SetLiftPose(_lift ? 1 : 5);
        }

        _xOld = X;
    }
}