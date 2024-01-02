package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module
public class OdometrsHandler implements IRobotModule {
    private DcMotor _odometrY, _odometrXLeft, _odometrXRight;

    @Override
    public void Init(BaseCollector collector){
        _odometrXLeft = Devices.OdometerXLeft;
        _odometrY = Devices.OdometerY;
        _odometrXRight = Devices.OdometerXRight;
    }

    @Override
    public void Start() {
        Reset();
    }

    @Override
    public void Update() {}

    @Override
    public void Stop() {}

    public double GetOdometrXLeft(){
        return _odometrXLeft.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometrXRigth(){
        return _odometrXRight.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometrY(){
        return _odometrY.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
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
