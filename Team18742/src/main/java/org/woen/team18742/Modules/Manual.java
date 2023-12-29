package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Tools.Vector2;

public class Manual {
    private final BaseCollector _collector;

    private boolean _brushReversOld = false, _brushOld = false;
    private boolean ferty = false;

    private final Plane _plane;

    public Manual(BaseCollector collector) {
        _collector = collector;

        _plane = new Plane(_collector.Time);
    }

    boolean oldgrip;

    public void Update() {
        _plane.Update();

        _collector.Driver.DriveDirection(
                new Vector2(_collector.Robot.gamepad1.left_stick_y, _collector.Robot.gamepad1.left_stick_x),
                _collector.Robot.gamepad1.right_stick_x);

        boolean A = _collector.Robot.gamepad1.square;
        boolean liftUp = _collector.Robot.gamepad1.dpad_up;
        boolean liftDown = _collector.Robot.gamepad1.dpad_down;
        boolean brushRevers = _collector.Robot.gamepad1.circle;
        boolean grip = _collector.Robot.gamepad1.triangle;
        boolean brush = _collector.Robot.gamepad1.cross;
        boolean zajat = _collector.Robot.gamepad1.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик
        boolean average = _collector.Robot.gamepad1.dpad_right;
        double railgunopen = _collector.Robot.gamepad1.left_trigger;
        double railgunnoopen = _collector.Robot.gamepad1.right_trigger;

        _plane.BezpolezniRailgunUp(railgunopen * 0.1);
        _plane.BezpolezniRailgunDown(railgunnoopen * 0.1);

        if(grip && !oldgrip) {
            ferty = !ferty;
            _collector.Intake.setGripper(ferty);
        }

        if(grip)
            _collector.Intake.releaseGripper();

        if (brush && !_brushOld) {
            if(_collector.Brush.IsIntake())
                _collector.Brush.Stop();
            else
                _collector.Brush.IntakePowerWithDefense();
        } else if(brushRevers && !_brushReversOld){
            if(_collector.Brush.IsRevers())
                _collector.Brush.Stop();
            else
                _collector.Brush.Revers();
        }

        if (A)
            _plane.Launch(zajat);
        else
            _plane.DeLaunch();

        if (liftUp && _collector.Intake.isPixelLocated)
            _collector.Lift.SetLiftPose(LiftPose.UP);

        if(liftDown)
            _collector.Lift.SetLiftPose(LiftPose.DOWN);

        if(average && _collector.Intake.isPixelLocated)
            _collector.Lift.SetLiftPose(LiftPose.AVERAGE);

        _brushOld = brush;

        _brushReversOld = brushRevers;

        oldgrip = grip;
    }
}