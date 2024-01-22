package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.PI;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.security.spec.EllipticCurve;

@Module
public class OdometryHandler implements IRobotModule {
    private DcMotorEx _odometerY, _odometerXLeft, _odometerXRight;

    private OdometrsOdometry _odometry;
    private CVOdometry _cvOdometry;
    private EncoderOdometry _encoderOdometry;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);

    public Vector2 Position = new Vector2();
    public Vector2 Speed = new Vector2();

    private final ElapsedTime _deltaTime = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
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
        Position = Bios.GetStartPosition().Position.clone();
    }

    public double GetSpeedOdometerXLeft() {
        return _odometerXLeft.getVelocity() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetSpeedOdometerXRight() {
        return -_odometerXRight.getVelocity() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetSpeedOdometerY() {
        return _odometerY.getVelocity() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerXLeft() {
        return _odometerXLeft.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerXRight() {
        return -_odometerXRight.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public double GetOdometerY() {
        return _odometerY.getCurrentPosition() / Configs.Odometry.EncoderconstatOdometr * PI * Configs.Odometry.DiametrOdometr;
    }

    public void Reset() {
        _odometerXLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerXLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _odometerXRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerXRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _odometerY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometerY.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        _filterX.Reset();
        _filterY.Reset();
        _deltaTime.reset();
    }

    @Override
    public void Update() {
        _filterX.UpdateCoef(Configs.Odometry.XCoef);
        _filterY.UpdateCoef(Configs.Odometry.YCoef);

        Vector2 pos = Configs.GeneralSettings.IsUseOdometers ? Vector2.Plus(Position, _odometry.ShiftPosition) : Vector2.Plus(Position, _encoderOdometry.ShiftPosition);

        if (!_cvOdometry.IsZero) {
            Position.X = _filterX.UpdateRaw(pos.X, _cvOdometry.Position.X - Position.X);
            Position.Y = _filterY.UpdateRaw(pos.Y, _cvOdometry.Position.Y - Position.Y);
        } else
            Position = pos;

        ToolTelemetry.DrawCircle(Position, 5, "#0000FF");
        ToolTelemetry.AddLine("position = " + Position.X + " " + Position.Y);

        Speed = _odometry.Speed;

        ToolTelemetry.AddLine("speed = " + Speed.X + " " + Speed.Y);

        _deltaTime.reset();
    }
}
