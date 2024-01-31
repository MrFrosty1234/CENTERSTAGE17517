package org.woen.team18742.Tools.Motor;

import java.util.ArrayList;

public class MotorsHandler {
    public static final ArrayList<Motor> _motors = new ArrayList<>();

    public static void AddMotor(Motor motor){
        _motors.add(motor);
    }

    public void Start(){
        for(Motor i : _motors)
            i.Start();
    }

    public MotorsHandler(){
        _motors.clear();
    }

    public void Update(){
        for(Motor i : _motors)
            i.Update();
    }
}
