package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.LiftPose;
@Config
public class Manual {
    private BaseCollector _collector;

    private boolean _brushOld = false, _isBrushOn = false;
    private boolean recuiert = false;
    private boolean ferty = false;
    public static double servoplaneOtkrit = 0.08;
    public static double servoplaneneOtkrit = 0.248;
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

    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);

        boolean A = _collector.CommandCode.gamepad1.square;
        boolean liftUp = _collector.CommandCode.gamepad1.dpad_up;
        boolean liftDown = _collector.CommandCode.gamepad1.dpad_down;
        boolean grip = _collector.CommandCode.gamepad1.triangle;
        boolean brush = _collector.CommandCode.gamepad1.cross;
        boolean zajat = _collector.CommandCode.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик

        if(grip && !oldgrip) {
            ferty = !ferty;
            _collector.Intake.setGripper(ferty);
        }

        if(grip)
            _collector.Intake.releaseGripper();

        oldgrip = grip;

        if(_collector.Lift.isDown()){
            if (brush && !_brushOld) {
                _isBrushOn = !_isBrushOn;
                _collector.Intake.intakePowerWithDefense(_isBrushOn);
            }
        }
        else
        {
            _isBrushOn = false;
            _collector.Intake.intakePowerWithDefense(_isBrushOn);
        }

        if (A && (zajat || _collector.Time.milliseconds() - _origmillis > 90000))
            _servoPlane.setPosition(servoplaneOtkrit);
        else{
            _servoPlane.setPosition(servoplaneneOtkrit);
        }
        if (liftUp) {
            //_collector.Intake.setClamp(true);
            _collector.Lift.SetLiftPose(LiftPose.UP);
        }

        if(liftDown)
            _collector.Lift.SetLiftPose(LiftPose.DOWN);

        _brushOld = brush;
    }
}