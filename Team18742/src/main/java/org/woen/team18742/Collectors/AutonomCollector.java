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
    }

    @Override
    public void Start() {
        super.Start();

        Odometry.Start();
        _camera.Start();

        switch (_camera.GetPosition()) {
            case FORWARD: {
                _route = new Runnable[]{
                        () -> {
                            Auto.PIDMove(-20, 0);
                        },
                        () -> {
                            Auto.PIDMove(5, 0);
                        },
                        () ->{
                            Auto.PIDMove(0, 60);
                        }
                };

                break;
            }

            case RIGHT: {
                _route = new Runnable[]{
                        () -> {
                            Auto.PIDMove(80, 5);
                        },
                        ()->{
                            Auto.PIDMove(-10, 60);
                        }
                };

                break;
            }

            default: {
                _route = new Runnable[]{
                        () -> {
                            Auto.PIDMove(60, 0);
                        },
                        () -> {
                            Auto.PIDMove(-10, 60);
                        }
                };
                break;
            }
        }
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

    @Override
    public void Stop() {
        _camera.Stop();
    }
}