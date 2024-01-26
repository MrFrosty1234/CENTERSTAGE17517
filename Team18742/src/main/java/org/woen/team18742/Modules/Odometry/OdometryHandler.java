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
import org.woen.team18742.Tools.Motor.EncoderControl;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.security.spec.EllipticCurve;

@Module
public class OdometryHandler implements IRobotModule {
    private EncoderControl _odometerY, _odometerXLeft, _odometerXRight;

    private OdometrsOdometry _odometry;
    private CVOdometry _cvOdometry;
    private EncoderOdometry _encoderOdometry;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);

    public Vector2 Speed = new Vector2();

    public Vector2 Position = new Vector2();

    private final Vector2 _maxSpeed = new Vector2();

    @Override
    public void Init(BaseCollector collector) {
        _odometerXLeft = new EncoderControl(Devices.OdometerXLeft, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerY = new EncoderControl(Devices.OdometerY, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerXRight = new EncoderControl(Devices.OdometerXRight, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);

        Devices.OdometerY.setDirection(DcMotorSimple.Direction.FORWARD);

        _odometry = collector.GetModule(OdometrsOdometry.class);
        _cvOdometry = collector.GetModule(CVOdometry.class);
        _encoderOdometry = collector.GetModule(EncoderOdometry.class);
    }

    @Override
    public void Start() {
        Reset();

        _odometerY.Start();
        _odometerXRight.Start();
        _odometerXLeft.Start();

        Position = Bios.GetStartPosition().Position.clone();
    }

    public double GetSpeedOdometerXLeft() {
        return _odometerXLeft.getVelocity();
    }

    public double GetSpeedOdometerXRight() {
        return _odometerXRight.getVelocity();
    }

    public double GetSpeedOdometerY() {
        return _odometerY.getVelocity();
    }

    public double GetOdometerXLeft() {
        return _odometerXLeft.GetPosition();
    }

    public double GetOdometerXRight() {
        return -_odometerXRight.GetPosition();
    }

    public double GetOdometerY() {
        return _odometerY.GetPosition();
    }

    public void Reset() {
        _odometerXLeft.Reset();
        _odometerXRight.Reset();
        _odometerY.Reset();

        _filterX.Reset();
        _filterY.Reset();
    }

    @Override
    public void LastUpdate() {
        _filterX.UpdateCoef(Configs.Odometry.XCoef);
        _filterY.UpdateCoef(Configs.Odometry.YCoef);

        _odometerY.Update();
        _odometerXRight.Update();
        _odometerXLeft.Update();

        Vector2 pos = Configs.GeneralSettings.IsUseOdometers ? Vector2.Plus(Position, _odometry.ShiftPosition) : Vector2.Plus(Position, _encoderOdometry.ShiftPosition);

        if (!_cvOdometry.IsZero) {
            Position.X = _filterX.UpdateRaw(pos.X, _cvOdometry.Position.X - Position.X);
            Position.Y = _filterY.UpdateRaw(pos.Y, _cvOdometry.Position.Y - Position.Y);
        } else
            Position = pos;

        ToolTelemetry.DrawCircle(Position, 5, "#0000FF");
        ToolTelemetry.AddLine("position = " + Position.X + " " + Position.Y);

        Speed = Configs.GeneralSettings.IsUseOdometers ? _odometry.Speed : _encoderOdometry.Speed;

        if (Speed.X > _maxSpeed.X)
            _maxSpeed.X = Speed.X;

        if (Speed.Y > _maxSpeed.Y)
            _maxSpeed.Y = Speed.Y;

        ToolTelemetry.AddLine("drive speed = " + Speed);
        ToolTelemetry.AddLine("max drive speed = " + _maxSpeed);
    }
}
