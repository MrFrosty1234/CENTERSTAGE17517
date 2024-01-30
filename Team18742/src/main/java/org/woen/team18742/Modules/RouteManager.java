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
                        _route.add(()->_automatic.PIDMove(new Vector2(-48, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, 30)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-65, 0)));
                        break;
                    }

                    case RIGHT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-39, 29), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(15, -40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(5, 0)));
                        _route.add(() -> _automatic.PIDMove(new Vector2(-80, 5)));
                        break;
                    }

                    case LEFT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-20, 5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-10, -18)));
                        _route.add(()->_automatic.PIDMove(new Vector2(10, 18)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-50, 5)));
                        break;
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-130, -130), PI / 2));

                switch (_camera.GetPosition()) {
                    case FORWARD:
                        _route.add(() -> _automatic.PIDMove(new Vector2(76, -98)));
                        break;

                    case RIGHT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(53, -102)));
                        break;

                    case LEFT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(86, -98)));
                        break;
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.MIDDLE_LOWER));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-123, -215), PI / 2));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, -30)));

                break;
            }

            case BLUE_BACK: {
                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-54, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, -40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-45, 0)));
                        break;
                    }

                    case RIGHT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-39, -29), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(15, 40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(5, 0)));
                        _route.add(() -> _automatic.PIDMove(new Vector2(-80, -5)));
                        break;
                    }

                    case LEFT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-20, -9)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-10, 22)));
                        _route.add(()->_automatic.PIDMove(new Vector2(10, -22)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-60, -5)));
                        break;
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-125, 130), -PI / 2));

                _route.add(()->_automatic.SetSpeed(0.3));

                switch (_camera.GetPosition()) {
                    case FORWARD:
                        _route.add(() -> _automatic.PIDMove(new Vector2(59, 98)));
                        break;

                    case RIGHT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(39, 102)));
                        break;

                    case LEFT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(78, 98)));
                        break;
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.MIDDLE_LOWER));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.SetSpeed(0.5));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-135, 215), -PI / 2));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, 30)));

                break;
            }

            case RED_FORWARD:{
                _automatic.SetSpeed(0.4);

                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-62, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(30, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, -10)));
                        break;
                    }

                    case LEFT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-55, -22), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(20, -20), 0));
                        break;
                    }

                    case RIGHT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, -5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-12, 15)));
                        _route.add(()->_automatic.PIDMove(new Vector2(12, 0)));
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-65, -92), PI / 2));

                switch (_camera.GetPosition()){
                    default:
                        break;

                    case LEFT:
                        _route.add(()->_automatic.PIDMove(new Vector2(40, 0)));

                    case RIGHT:
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 0)));
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.MIDDLE_LOWER));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-10, -76)));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, -20)));

                break;
            }

            default:{
                _automatic.SetSpeed(0.4);

                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-62, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(30, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, 10)));
                        break;
                    }

                    case LEFT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-55, 22), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(20, 20), 0));
                        break;
                    }

                    case RIGHT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-12, -15)));
                        _route.add(()->_automatic.PIDMove(new Vector2(12, 0)));
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-65, 92), PI / 2));

                switch (_camera.GetPosition()){
                    default:
                        break;

                    case LEFT:
                        _route.add(()->_automatic.PIDMove(new Vector2(40, 0)));

                    case RIGHT:
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 0)));
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.MIDDLE_LOWER));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-10, 76)));
                _route.add(() -> _automatic.PIDMove(new Vector2(0,  20)));

                break;
            }
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
