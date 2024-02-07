package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

public class Plane {
    private final Servo _servoPlane;
    private final ElapsedTime _time;

    public Plane(ElapsedTime time){
        _time = time;
        _servoPlane = Devices.ServoPlane;
    }
    public void Launch(boolean debug){
        if(debug || _time.milliseconds() > 60000)
            _servoPlane.setPosition(Configs.Plane.servoplaneOtkrit);
    }

    public void DeLaunch(){
        _servoPlane.setPosition(Configs.Plane.servoplaneneOtkrit);
    }
}
