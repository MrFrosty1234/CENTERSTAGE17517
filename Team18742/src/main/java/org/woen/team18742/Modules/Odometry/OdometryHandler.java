package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class OdometryHandler implements IRobotModule {
    private DcMotor _odometerY, _odometerXLeft, _odometerXRight;

    private OdometrsOdometry _odometry;
    private CVOdometry _cvOdometry;
    private EncoderOdometry _encoderOdometry;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);
    private final ExponentialFilter _filterSpeedX = new ExponentialFilter(Configs.Odometry.XSpeedCoef), _filterSpeedY = new ExponentialFilter(Configs.Odometry.YSpeedCoef);

    public Vector2 Position = new Vector2();
    public Vector2 Speed = new Vector2();

    @Override
    public void Init(BaseCollector collector){
        _odometerXLeft = Devices.OdometerXLeft;
        _odometerY = Devices.OdometerY;
        _odometerXRight = Devices.OdometerXRight;

        _odometerY.setDirection(DcMotorSimple.Direction.FORWARD);

        _odometry = collector.GetModule(OdometrsOdometry.class);
        _cvOdometry = collector.GetModule(CVOdometry.class);
        _encoderOdometry = collector.GetModule(EncoderOdometry.class);
    }

    @Override
    public void Start() {
        Reset();
        Position = AutonomCollector.StartPosition.Position.copy();
    }

    public double GetOdometerXLeft(){
        return _odometerXLeft.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerXRight(){
        return -_odometerXRight.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
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

        _filterX.Reset();
        _filterY.Reset();
        _filterSpeedX.Reset();
        _filterSpeedY.Reset();
    }

    @Override
    public void Update() {
        _filterX.UpdateCoef(Configs.Odometry.XCoef);
        _filterY.UpdateCoef(Configs.Odometry.YCoef);
        _filterSpeedX.UpdateCoef(Configs.Odometry.XSpeedCoef);
        _filterSpeedY.UpdateCoef(Configs.Odometry.YSpeedCoef);

        ToolTelemetry.AddLine("odometrY = " + GetOdometerY());
        ToolTelemetry.AddLine("odometrXLeft = " + GetOdometerXLeft());
        ToolTelemetry.AddLine("odometrXRight = " + GetOdometerXRight());

        ToolTelemetry.DrawCircle(_encoderOdometry.Position, 5, "#000000");
        ToolTelemetry.DrawCircle(_odometry.Position, 5, "#555555");
        ToolTelemetry.DrawCircle(_cvOdometry.Position, 5, "#AAAAAA");

        Vector2 pos = Configs.GeneralSettings.IsUseOdometers ? _odometry.Position : _encoderOdometry.Position;
        Vector2 speed = Configs.GeneralSettings.IsUseOdometers ? _odometry.Speed : _encoderOdometry.Speed;

        Position.X = _filterX.Update(Position.X, pos.X);
        Position.Y = _filterY.Update(Position.Y, pos.Y);
        Speed.X = _filterX.Update(Speed.X, speed.X);
        Speed.Y = _filterX.Update(Speed.Y, speed.Y);
    }
}
