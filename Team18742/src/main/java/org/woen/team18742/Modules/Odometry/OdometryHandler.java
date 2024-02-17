package org.woen.team18742.Modules.Odometry;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Color;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.Motor.EncoderControl;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class OdometryHandler implements IRobotModule {
    private EncoderControl _odometerY, _odometerXLeft, _odometerXRight;

    private OdometrsOdometry _odometry;
    private CVOdometry _cvOdometry;
    private DriveEncoderOdometry _encoderOdometry;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);
    private Vector2 _oldSpeed = new Vector2();

    public Vector2 Position = new Vector2(), Speed = new Vector2(), Accel = new Vector2();

    private final Vector2 _maxSpeed = new Vector2(), _maxAccel = new Vector2();
    private final ElapsedTime _deltaTime = new ElapsedTime();

    private static boolean _isInited = false;

    @Override
    public void Init(BaseCollector collector) {
        _odometerXLeft = new EncoderControl(Devices.OdometerXLeft, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerY = new EncoderControl(Devices.OdometerY, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);
        _odometerXRight = new EncoderControl(Devices.OdometerXRight, Configs.Odometry.EncoderconstatOdometr, Configs.Odometry.DiametrOdometr);

        Devices.OdometerY.setDirection(DcMotorSimple.Direction.FORWARD);

        _odometry = collector.GetModule(OdometrsOdometry.class);
        _cvOdometry = collector.GetModule(CVOdometry.class);
        _encoderOdometry = collector.GetModule(DriveEncoderOdometry.class);
    }

    @Override
    public void Start() {
        _odometerY.Start();
        _odometerXRight.Start();
        _odometerXLeft.Start();

        Position = Bios.GetStartPosition().Position.clone();

        Reset();
    }

    public double GetSpeedOdometerXLeft() {
        return _odometerXLeft.GetVelocity();
    }

    public double GetSpeedOdometerXRight() {
        return -_odometerXRight.GetVelocity();
    }

    public double GetSpeedOdometerY() {
        return _odometerY.GetVelocity();
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

        _deltaTime.reset();
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

        ToolTelemetry.DrawCircle(pos, Configs.DriveTrainWheels.Radius, Color.BLUE);

        Speed = Configs.GeneralSettings.IsUseOdometers ? _odometry.Speed : _encoderOdometry.Speed;

        Accel.X = (Speed.X - _oldSpeed.X) / _deltaTime.seconds();
        Accel.Y = (Speed.Y - _oldSpeed.Y) / _deltaTime.seconds();

        if (Math.abs(Speed.X) > _maxSpeed.X)
            _maxSpeed.X = Math.abs(Speed.X);

        if (Math.abs(Speed.Y) > _maxSpeed.Y)
            _maxSpeed.Y = Math.abs(Speed.Y);

        if (Math.abs(Accel.X) > _maxAccel.X)
            _maxAccel.X = Math.abs(Accel.X);

        if (Math.abs(Accel.Y) > _maxAccel.Y)
            _maxAccel.Y = Math.abs(Accel.Y);

        ToolTelemetry.AddLine("pos " + Position);
        ToolTelemetry.AddLine("drive speed " + Speed);
        ToolTelemetry.AddLine("drive accel " + Accel);
        ToolTelemetry.AddLine("max drive speed " + _maxSpeed);
        ToolTelemetry.AddLine("max drive accel " + _maxAccel);

        _oldSpeed = Speed.clone();
        _deltaTime.reset();
    }
}
