package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.TeleopModule;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.Vector2;

@TeleopModule
public class Manual implements IRobotModule {
    private boolean _gripOld = false, _brushOld = false;

    private Plane _plane;

    private Gamepad _gamepad;

    private Brush _brush;
    private Intake _intake;
    private Lift _lift;
    private Drivetrain _drivetrain;
    private Suspension _suspension;

    @Override
    public void Init(BaseCollector collector) {
        _plane = new Plane(collector.Time);

        _gamepad = collector.Robot.gamepad1;

        _brush = collector.GetModule(Brush.class);
        _intake = collector.GetModule(Intake.class);
        _lift = collector.GetModule(Lift.class);
        _drivetrain = collector.GetModule(Drivetrain.class);
        _suspension = collector.GetModule(Suspension.class);

        LiftPose.AVERAGE.Pose = 400;
    }

    @Override
    public void Update() {
        _plane.Update();

        _drivetrain.SimpleDriveDirection(
                new Vector2(_gamepad.left_stick_y, _gamepad.left_stick_x),
                _gamepad.right_stick_x);

        boolean A = _gamepad.square;
        boolean liftUp = _gamepad.dpad_up;
        boolean grip = _gamepad.triangle;
        boolean brush = _gamepad.cross;
        boolean zajat = _gamepad.left_bumper;// зажать эту кнопку чтоб досрочно запустить самолетик
        boolean average = _gamepad.dpad_right;
        double servotyaga = _gamepad.left_trigger;


        if(grip && !_gripOld) {
            _intake.releaseGripper();
        }

        if(brush && !_brushOld){
            if(_brush.isBrusnOn())
                _brush.BrushEnable();
            else {
                _brush.BrushDisable();
                _brush.RevTimeRes();
            }
        }

        if (A)
            _plane.Launch(zajat);
        else
            _plane.DeLaunch();

        if (liftUp)
            _lift.SetLiftPose(LiftPose.UP);
        else if(average)
            _lift.SetLiftPose(LiftPose.AVERAGE);

        if(servotyaga > 0.2)
            _suspension.Active();
        else
            _suspension.Disable();

        _gripOld = grip;
        _brushOld = brush;
    }

    @Override
    public void Start() {
        _intake.setGripper(false);
    }
}