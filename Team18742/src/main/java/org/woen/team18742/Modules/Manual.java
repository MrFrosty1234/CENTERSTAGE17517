package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Lift.LiftPose;
@Config
public class Manual {
    private BaseCollector _collector;

    private boolean _clampOld = false, _isBrushOn = false;
    private boolean _XOld = false, _clampOpen = false;
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
    boolean oldperevprot;

    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);

        boolean A = _collector.CommandCode.gamepad1.square;
        boolean X = false;
        boolean liftUp = _collector.CommandCode.gamepad1.dpad_up;
        boolean liftDown = _collector.CommandCode.gamepad1.dpad_down;
        boolean grip = _collector.CommandCode.gamepad1.triangle;
        boolean clamp = _collector.CommandCode.gamepad1.cross;
        boolean perevert = _collector.CommandCode.gamepad1.circle;
        boolean zajat = _collector.CommandCode.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик

        if(grip && !oldgrip) {
            ferty = !ferty;
            _collector.Intake.setGripper(ferty);
        }

        if(grip)
            _collector.Intake.releaseGripper();

        oldgrip = grip;

        if(!_collector.Lift.isUp()) {
            recuiert = false;
            _collector.Intake.setperevorotik(false);
        }
        else {
            _collector.Intake.setperevorotik(recuiert);
            if (perevert && !oldperevprot)
                recuiert = !recuiert;
        }

        if(_collector.Lift.isDown()){
            if (clamp && !_clampOld) {
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
            _clampOpen = true;
            _collector.Intake.setClamp(true);
            _collector.Lift.SetLiftPose(LiftPose.UP);
        }

        if(liftDown)
            _collector.Lift.SetLiftPose(LiftPose.DOWN);

        if(X && !_XOld){
            _clampOpen = !_clampOpen;
            _collector.Intake.setClamp(_clampOpen);
        }

        _clampOld = clamp;
        oldperevprot = perevert;
        _XOld = X;
    }
}