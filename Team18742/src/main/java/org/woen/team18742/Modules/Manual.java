package org.woen.team18742.Modules;

import static java.lang.Math.PI;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.TeleopModule;
import org.woen.team18742.Tools.Vector2;

@TeleopModule
public class Manual implements IRobotModule {
    private boolean _gripOld = false;

    private Plane _plane;

    private Gamepad _gamepad;

    private Brush _brush;
    private Intake _intake;
    private Lift _lift;
    private Drivetrain _drivetrain;

    @Override
    public void Init(BaseCollector collector) {
        _plane = new Plane(collector.Time);

        _gamepad = collector.Robot.gamepad1;

        _brush = collector.GetModule(Brush.class);
        _intake = collector.GetModule(Intake.class);
        _lift = collector.GetModule(Lift.class);
        _drivetrain = collector.GetModule(Drivetrain.class);

        LiftPose.AVERAGE.Pose = 400;
    }

    @Override
    public void Update() {
        _plane.Update();

        _drivetrain.SimpleDriveDirection(
                new Vector2(_gamepad.left_stick_y, _gamepad.left_stick_x),
                _gamepad.right_stick_x);


        /*_drivetrain.SetCMSpeed(
                new Vector2(_gamepad.left_stick_y * 100, _gamepad.left_stick_x * 100),
                _gamepad.right_stick_x * toRadians(180));*/

        boolean A = _gamepad.square;
        boolean liftUp = _gamepad.dpad_up;
        boolean brushRevers = _gamepad.circle;
        boolean grip = _gamepad.triangle;
        boolean brush = _gamepad.cross;
        boolean najat = _gamepad.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик
        boolean average = _gamepad.dpad_right;
        double railgunopen = _gamepad.left_trigger;
        double railgunnoopen = _gamepad.right_trigger;

        _plane.BezpolezniRailgunUp(railgunopen * 0.3);
        _plane.BezpolezniRailgunDown(railgunnoopen * 0.3);

        if (grip && !_gripOld) {
            _intake.releaseGripper();
        }

        if (brush) {
            _brush.BrushEnable();
        }
        if (brushRevers) {
            _brush.BrushDisable();
            _brush.RevTimeRes();
        }

        if (A)
            _plane.Launch(najat);
        else
            _plane.DeLaunch();

        if (liftUp)
            _lift.SetLiftPose(LiftPose.UP);
        else if (average)
            _lift.SetLiftPose(LiftPose.AVERAGE);

        _gripOld = grip;
    }

    @Override
    public void Start() {
        _intake.setGripper(false);
    }
}