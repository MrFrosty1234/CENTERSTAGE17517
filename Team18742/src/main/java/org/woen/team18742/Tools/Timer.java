package org.woen.team18742.Tools;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Timer {
    private ElapsedTime _timer = new ElapsedTime();
    private boolean _isActive = false;
    private double _time;

    private Runnable _action;

    public Timer(){
        _timer.reset();
        TimerHandler.AddTimer(this);
    }

    public void Start(double time, Runnable action){
        _timer.reset();
        _isActive = true;
        _time = time;

        _action = action;
    }

    public void Update(){
        if(_isActive && _timer.milliseconds() > _time){
            _isActive = false;

            _action.run();
        }
    }

    public void Stop(){
        _isActive = false;
    }

    public boolean IsActive(){
        return _isActive;
    }
}
