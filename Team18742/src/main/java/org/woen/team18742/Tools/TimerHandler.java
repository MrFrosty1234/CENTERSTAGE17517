package org.woen.team18742.Tools;

import java.util.ArrayList;

public class TimerHandler {
    private static ArrayList<Timer> _timers = new ArrayList<>();

    public static void AddTimer(Timer timer){
        _timers.add(timer);
    }

    public void Update(){
        for(Timer i : _timers)
            i.Update();
    }

    public TimerHandler(){
        _timers.clear();
    }
}
