package org.woen.team18742.Tools.Timers;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ElapsedTimeExtra extends ElapsedTime {
    public void pause(){
        if(_isPause)
            return;

        _nsPauseStart = nanoseconds();

        _isPause = true;

        _pauseTime.reset();
    }

    public void start(){
        if(!_isPause)
            return;

        _isPause = false;

        nsStartTime += _pauseTime.nanoseconds();
    }

    @Override
    public long nanoseconds() {
        if(_isPause)
            return _nsPauseStart;

        return super.nanoseconds();
    }

    private final ElapsedTime _pauseTime = new ElapsedTime();
    private long _nsPauseStart;
    private boolean _isPause = false;

    public boolean IsPause(){
        return _isPause;
    }
}
