package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;

public class OdometrsHandler {
    private final DcMotor _odometrY, _odometrXLeft, _odometrXRight;
    public static double RadiusOdometr = 5; //random number
    private final double _diametrOdometr = 4.8, _encoderconstatOdometr = 1440;

    public OdometrsHandler(BaseCollector collector){
        _odometrXLeft = Devices.OdometrXLeft;
        _odometrY = Devices.OdometrY;
        _odometrXRight = Devices.OdometrXRight;
    }

    public double GetOdometrXLeft(){
        return _odometrXLeft.getCurrentPosition() / _encoderconstatOdometr * PI * _diametrOdometr;
    }

    public double GetOdometrXRigth(){
        return _odometrXRight.getCurrentPosition() / _encoderconstatOdometr * PI * _diametrOdometr;
    }

    public double GetOdometrY(){
        return _odometrY.getCurrentPosition() / _encoderconstatOdometr * PI * _diametrOdometr;
    }

    public void Reset(){
        _odometrXLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometrXLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _odometrXRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometrXRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _odometrY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometrY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
