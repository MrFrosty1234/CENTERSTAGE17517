package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.PI;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
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
    private DcMotor _odometerY, _odometerXLeft, _odometerXRight;

    private OdometrsOdometry _odometry;
    private CVOdometry _cvOdometry;
    private EncoderOdometry _encoderOdometry;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);

    public Vector2 Position = new Vector2();
    public Vector2 Speed = new Vector2();
    private Vector2 _oldPosition = new Vector2();

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
        Position = Bios.GetStartPosition().Position.copy();
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

        ToolTelemetry.AddLine("odometrY = " + GetOdometerY());
        ToolTelemetry.AddLine("odometrXLeft = " + GetOdometerXLeft());
        ToolTelemetry.AddLine("odometrXRight = " + GetOdometerXRight());

        ToolTelemetry.DrawCircle(_encoderOdometry.Position, 5, "#000000");
        ToolTelemetry.DrawCircle(_odometry.Position, 5, "#FF0000");
        ToolTelemetry.DrawCircle(_cvOdometry.Position, 5, "#00FF00");

        Vector2 pos = Configs.GeneralSettings.IsUseOdometers ? Vector2.Plus(Position, _odometry.ShiftPosition) : Vector2.Plus(Position, _encoderOdometry.ShiftPosition);

        if (!_cvOdometry.IsZero) {
            Position.X = _filterX.UpdateRaw(pos.X, Position.X - _cvOdometry.Position.X);
            Position.Y = _filterY.UpdateRaw(pos.Y, Position.Y - _cvOdometry.Position.Y);
        } else
            Position = pos;

        ToolTelemetry.DrawCircle(Position, 5, "#0000FF");

        Speed.X = _oldPosition.X - Position.X / _deltaTime.seconds();
        Speed.Y = _oldPosition.Y - Position.Y / _deltaTime.seconds();

        _oldPosition = Position.copy();
        _deltaTime.reset();
    }
}
