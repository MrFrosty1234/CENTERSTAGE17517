package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module
public class OdometryHandler implements IRobotModule {
    private DcMotor _odometerY, _odometerXLeft, _odometerXRight;

    @Override
    public void Init(BaseCollector collector){
        _odometerXLeft = Devices.OdometerXLeft;
        _odometerY = Devices.OdometerY;
        _odometerXRight = Devices.OdometerXRight;
    }

    @Override
    public void Start() {
        Reset();
    }

    public double GetOdometerXLeft(){
        return _odometerXLeft.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerXRight(){
        return _odometerXRight.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerY(){
        return _odometerY.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public void Reset(){
        _odometerXLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerXLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _odometerXRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerXRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _odometerY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
