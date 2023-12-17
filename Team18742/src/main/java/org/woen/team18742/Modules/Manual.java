package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.LiftPose;

@Config
public class Manual {
    private final BaseCollector _collector;

    private boolean _isBrushOn = false, _brushReversOld = false;
    private boolean _brushReverseOn = false;
    private boolean _brushOld = false;
    private boolean ferty = false;
    public static double servoplaneOtkrit = 0.5;
    public static double servoplaneneOtkrit = 0.07;
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
        boolean brushRevers = _collector.CommandCode.gamepad1.dpad_left;
        boolean grip = _collector.CommandCode.gamepad1.triangle;
        boolean brush = _collector.CommandCode.gamepad1.cross;
        boolean zajat = _collector.CommandCode.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик
        boolean average = _collector.CommandCode.gamepad1.dpad_right;

        if(grip && !oldgrip) {
            ferty = !ferty;
            _collector.Intake.setGripper(ferty);
        }

        if(grip)
            _collector.Intake.releaseGripper();

        oldgrip = grip;

        if(_collector.Lift.isDown()){
            if (brush && !_brushOld) {
                _brushReverseOn = false;
                _isBrushOn = !_isBrushOn;
                _collector.Brush.intakePowerWithDefense(_isBrushOn);
            } else if(brushRevers && !_brushReversOld){
                _isBrushOn = false;
                _collector.Brush.intakePowerWithDefense(_isBrushOn);
                _brushReverseOn = !_brushReverseOn;
                _collector.Brush.reversbrush(_brushReverseOn ? -1 : 0);
            }
        }
        else
        {
            _collector.Brush.reversbrush(0);
            _isBrushOn = false;
            _brushReverseOn = false;
            _collector.Brush.intakePowerWithDefense(_isBrushOn);
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

        if(liftDown) {
            _collector.Lift.SetLiftPose(LiftPose.DOWN);
        }

        if(average)
            _collector.Lift.SetLiftPose(LiftPose.AVERAGE);

        _brushOld = brush;

        _brushReversOld = brushRevers;
    }
}