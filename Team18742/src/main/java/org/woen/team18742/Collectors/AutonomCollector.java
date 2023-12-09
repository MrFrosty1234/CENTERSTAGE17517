package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Odometry.Odometry;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public org.woen.team18742.Odometry.Odometry Odometry;
    private Camera _camera;

    private Runnable _route[];

    private int _currentRouteAction = 0;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Odometry = new Odometry(this);
        Auto = new Automatic(this);
        _camera = new Camera(this);

        switch (_camera.GetPosition()) {
            case FORWARD_LEFT: {
                _route = new Runnable[]{
                        () -> {
                            Intake.intakePowerWithDefense(true);
                            Auto.PIDMove(20, 0);
                        },
                        () -> {
                            Intake.intakePowerWithDefense(false);
                            Auto.PIDMove(0, -50);
                        }
                };

                break;
            }

            case FORWARD_RIGHT: {
                _route = new Runnable[]{
                        () -> {
                            Auto.PIDMove(50, 0);
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

        if (Auto.isMovedEnd() && Lift.isATarget()) {
            if (_currentRouteAction < _route.length) {
                _route[_currentRouteAction].run();

                _currentRouteAction++;
            } else
                Driver.Stop();
        }
    }
}
