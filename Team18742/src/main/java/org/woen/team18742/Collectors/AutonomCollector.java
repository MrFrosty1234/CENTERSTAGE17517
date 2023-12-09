package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Lift.LiftPose;
import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Odometry.Odometry;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public org.woen.team18742.Odometry.Odometry Odometry;
    private Camera _camera;

    private Runnable _route[];

    private int _currentRouteAction = 0;
    private boolean _isPixelWait = false;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Odometry = new Odometry(this);
        Auto = new Automatic(this);
        _camera = new Camera(this);

        switch (_camera.GetPosition()) {
            case FORWARD_LEFT: {
                _route = new Runnable[]{
                        () -> {
                            _isPixelWait = true;
                            Intake.intakePowerWithDefense(true);
                            Auto.PIDMove(20, 0);
                        },
                        () -> {
                            Intake.intakePowerWithDefense(false);
                            Lift.SetLiftPose(LiftPose.UP);
                            Auto.PIDMove(0, -50);
                        },
                        () ->{
                            Intake.setperevorotik(true);
                            Intake.setGripper(false);
                        }
                };

                break;
            }

            case FORWARD_RIGHT: {
                _route = new Runnable[]{
                        () -> {
                            _isPixelWait = true;
                            Intake.intakePowerWithDefense(true);
                            Auto.PIDMove(20, 0);
                        },
                        () -> {
                            Intake.intakePowerWithDefense(false);
                            Lift.SetLiftPose(LiftPose.UP);
                            Auto.PIDMove(0, 50);
                        },
                        () ->{
                            Intake.setperevorotik(true);
                            Intake.setGripper(false);
                        }
                };

                break;
            }

            default:
                break;
        }
    }

    @Override
    public void Start() {
        super.Start();

        Odometry.Start();
        _camera.Start();
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();

        Auto.Update();

        _camera.Update();

        if (Auto.isMovedEnd() && Lift.isATarget() && (!_isPixelWait || Intake.isPixelLocated)) {
            if (_currentRouteAction < _route.length) {
                _isPixelWait = false;
                _route[_currentRouteAction].run();

                _currentRouteAction++;
            } else
                Driver.Stop();
        }
    }
}
