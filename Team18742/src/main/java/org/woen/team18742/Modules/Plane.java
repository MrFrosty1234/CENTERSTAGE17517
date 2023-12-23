package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Tools.Devices;

@Config
public class Plane {
    private final Servo _servoPlane;

    private final Servo _servorailgun;
    public static double servoplaneOtkrit = 0.07;
    public static double servoplaneneOtkrit = 0.17;
    private final ElapsedTime _time;
    private double _oldTime = 0;
    private double pos;

    public Plane(ElapsedTime time){
        _time = time;
        _servoPlane = Devices.ServoPlane;
        _servorailgun = Devices.ServoRailGun;
    }
    public void Launch(boolean debug){
        if(debug)
            _servoPlane.setPosition(servoplaneOtkrit);
    }

    public void DeLaunch(){
        _servoPlane.setPosition(servoplaneneOtkrit);
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
