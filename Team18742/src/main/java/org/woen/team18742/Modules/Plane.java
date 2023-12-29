package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;

public class Plane {
    private final Servo _servoPlane;

    private final Servo _servorailgun;
    private final ElapsedTime _time;
    private double _oldTime = 0;
    private double pos;

    public Plane(ElapsedTime time){
        _time = time;
        _servoPlane = Devices.ServoPlane;
        _servorailgun = Devices.ServoRailGun;
    }
    public void Launch(boolean debug){
        if(debug || _time.milliseconds() > 60000)
            _servoPlane.setPosition(Configs.Plane.servoplaneOtkrit);
    }

    public void DeLaunch(){
        _servoPlane.setPosition(Configs.Plane.servoplaneneOtkrit);
    }

    public void BezpolezniRailgunUp(double step)
    {
        pos += step / (_time.milliseconds() - _oldTime);
        pos = Math.min(1, pos);

        _servorailgun.setPosition(pos);
    }

    public void BezpolezniRailgunDown(double step)
    {
        pos -= step / (_time.milliseconds() - _oldTime);

        pos = Math.max(0, pos);

        _servorailgun.setPosition(pos);
    }

    public void Update(){
        _oldTime = _time.milliseconds();
    }
}
