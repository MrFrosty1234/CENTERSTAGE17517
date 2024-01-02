package org.woen.team18742.Modules;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Lift.Lift;
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
    private boolean _isPixelWait = false;

    private Automatic _automatic;
    private Lift _lift;
    private Intake _intake;
    private AutonomCollector _collector;
    private Camera _camera;

    @Override
    public void Init(BaseCollector collector) {
        _automatic = collector.GetModule(Automatic.class);
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);

        if(collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    @Override
    public void Start() {
        if(_collector == null)
            return;

        switch (_camera.GetPosition()) {
            case FORWARD: {
                _route.add(()-> _automatic.PIDMove(new Vector2(-54, 0)));

                break;
            }

            case RIGHT: {
                _route.add(()-> _automatic.PIDMove(new Vector2(-45, 20)));

                break;
            }

            default: {
                _route.add(()-> _automatic.PIDMove(new Vector2(-45, -20)));

                break;
            }
        }
    }

    @Override
    public void Update() {
        if (Configs.GeneralSettings.IsAutonomEnable.Get()) {
            if (_automatic.isMovedEnd() && _lift.isATarget() && (!_isPixelWait || _intake.isPixelLocated)) {
                if (_currentRouteAction < _route.size()) {
                    _isPixelWait = false;
                    _route.get(_currentRouteAction).run();

                    _currentRouteAction++;
                }
            }
        }
    }

    @Override
    public void Stop() {}
}
