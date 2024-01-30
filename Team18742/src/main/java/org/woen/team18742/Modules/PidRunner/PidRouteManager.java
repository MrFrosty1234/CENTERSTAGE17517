package org.woen.team18742.Modules.PidRunner;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.PidRunner.PidAutomatic;

import java.util.ArrayList;
import java.util.List;

@AutonomModule
public class PidRouteManager implements IRobotModule {
    private List<Runnable> _route;

    private int _currentRouteAction = 0;
    private double _waitTime = -1;
    private boolean _isPixelWait = false;

    private PidAutomatic _automatic;
    private Lift _lift;
    private Intake _intake;
    private ElapsedTime _timer = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _automatic = collector.GetModule(PidAutomatic.class);
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
    }

    private void Wait(double sleep) {
        _timer.reset();
        _waitTime = sleep;
    }

    private boolean _isEnable = false;

    public void Start(ArrayList<Runnable> route){
        _route = route;
        _isEnable = true;
        _automatic.Start();
    }

    public boolean IsEnd(){
        return !_isEnable;
    }

    @Override
    public void Stop(){
        _isEnable = false;
        _automatic.Disable();
    }

    @Override
    public void LastUpdate() {
        if (_isEnable && _automatic.isMovedEnd() && _lift.isATarget() && (!_isPixelWait || _intake.isPixelGripped()) && _timer.milliseconds() > _waitTime) {
            if (_currentRouteAction < _route.size()) {
                _isPixelWait = false;
                _route.get(_currentRouteAction).run();

                _currentRouteAction++;
            }
            else {
                Stop();
            }
        }
    }
}

