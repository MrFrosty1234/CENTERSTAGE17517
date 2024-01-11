package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;
import java.util.List;

@AutonomModule
public class RouteManager implements IRobotModule {
    private final List<Runnable> _route = new ArrayList<>();

    private int _currentRouteAction = 0;
    private double _waitTime = -1;
    private boolean _isPixelWait = false;

    private Automatic _automatic;
    private Lift _lift;
    private Intake _intake;
    private AutonomCollector _collector;
    private Camera _camera;
    private boolean _isTurnWait = false;

    private ElapsedTime _timer = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _automatic = collector.GetModule(Automatic.class);
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);

        if (collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    private void Wait(double sleep) {
        _timer.reset();
        _waitTime = sleep;
    }

    @Override
    public void Start() {
        if(_collector == null)
            return;

        _intake.setGripper(true);

        _timer.reset();

        switch (AutonomCollector.StartPosition) {
            case RED_BACK: {
                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-42, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(25, 0), PI / 2));
                        _route.add(() -> _automatic.PIDMove(new Vector2(5, 0)));
                        break;
                    }

                    case RIGHT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 30), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(28, -30), PI / 2));
                        _route.add(() -> _automatic.PIDMove(new Vector2(0, -10)));
                        break;
                    }

                    case LEFT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-50, 5), PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, -15)));
                        _route.add(()->_automatic.PIDMove(new Vector2(45, 5)));
                        break;
                    }
                }

                _route.add(() -> _automatic.PIDMove(new Vector2(0, -100)));
                switch (_camera.GetPosition()) {
                    case FORWARD:
                        _route.add(() -> _automatic.PIDMove(new Vector2(-50, -110)));
                        break;

                    case RIGHT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(-70, -110)));
                        break;
                }
                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> _isTurnWait = true);
                _route.add(() -> Wait(1000));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                break;
            }

            case RED_FORWARD:

                break;

            case BLUE_BACK: {
                _route.add(() -> _automatic.PIDMove(new Vector2(0, -7), 0));
                _route.add(() -> _automatic.PIDMove(new Vector2(-80, 0)));
                _route.add(() -> _automatic.PIDMove(new Vector2(-120, -50)));
                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> _isTurnWait = true);
                _route.add(() -> Wait(1000));
                _route.add(() -> _intake.setGripper(false));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));
                break;
            }

            case BLUE_FORWAD:

                break;
        }
    }

    @Override
    public void LastUpdate() {
        if (Configs.GeneralSettings.IsAutonomEnable) {
            if (_automatic.isMovedEnd() && _lift.isATarget() && (!_isPixelWait || _intake.isPixelGripped()) && _timer.milliseconds() > _waitTime) {
                if (_currentRouteAction < _route.size()) {
                    _isPixelWait = false;
                    _route.get(_currentRouteAction).run();

                    _currentRouteAction++;
                }
            }
        }
    }
}
