package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.RobotPosition;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Odometry.Odometry;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public org.woen.team18742.Modules.Odometry.Odometry Odometry;
    public Camera Camera;

    private Runnable _route[];

    private int _currentRouteAction = 0;
    private boolean _isPixelWait = false;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Camera = new Camera(this);
        Odometry = new Odometry(this);
        Auto = new Automatic(this);
        Camera.Build();
    }

    @Override
    public void Start() {
        super.Start();

        Odometry.Start();

        switch (Camera.GetPosition()) {
            case FORWARD: {
                _route = new Runnable[]{
                        () -> {
                            Auto.PIDMove(-30, 0);

                            _isPixelWait = true;

                            Brush.intakePowerWithDefense(true);
                        },
                        () -> {
                            Lift.SetLiftPose(LiftPose.UP);
                            Auto.PIDMove(0, 60);
                        },
                        () -> {
                            Auto.PIDMove(30, 0);
                            Intake.setClamp(false);
                        },
                        () -> {
                            Auto.PIDMove(0, -60);
                            Lift.SetLiftPose(LiftPose.DOWN);
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

        Camera.GetPosition();

        if (Auto.isMovedEnd() && Lift.isATarget() && (!_isPixelWait || Intake.isPixelLocated)) {
            if (_currentRouteAction < _route.length) {
                _isPixelWait = false;
                //_route[_currentRouteAction].run();

                _currentRouteAction++;
            } else
                Driver.Stop();
        }
    }

    @Override
    public void Stop() {
        Camera.Stop();
    }
}