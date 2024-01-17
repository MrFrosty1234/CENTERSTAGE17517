package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Vector2;

@AutonomModule
public class EncoderOdometry implements IRobotModule {
    private Drivetrain _driverTrain;
    private Vector2  EncoderPosition = new Vector2();
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;
    private Gyroscope _gyro;
    public Vector2 Position = new Vector2();
    private final ElapsedTime _deltaTime = new ElapsedTime();
    public Vector2 Speed = new Vector2();

    @Override
    public void Init(BaseCollector collector) {
        _driverTrain = collector.GetModule(Drivetrain.class);
        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Update() {
        double lfd = _driverTrain.GetLeftForwardEncoder();
        double lbd = _driverTrain.GetLeftBackEncoder();
        double rfd = _driverTrain.GetRightForwardEncoder();
        double rbd = _driverTrain.GetRightBackEncoder();

        double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

        double deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
        double deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

        deltaY = deltaY * Configs.Odometry.YLag;

        _leftForwardDrive = lfd;
        _leftBackDrive = lbd;
        _rightBackDrive = rbd;
        _rightForwardDrive = rfd;

        Vector2 shift = new Vector2(deltaX *
                cos(_gyro.GetRadians()) +
                deltaY * sin(_gyro.GetRadians()),
                -deltaX * sin(_gyro.GetRadians()) +
                        deltaY * cos(_gyro.GetRadians()));

        Position = Vector2.Plus(shift, Position);

        Speed.X = shift.X / _deltaTime.seconds();
        Speed.Y = shift.Y / _deltaTime.seconds();
    }

    @Override
    public void Start() {
        _deltaTime.reset();

        Position = AutonomCollector.StartPosition.Position.copy();
    }
}
