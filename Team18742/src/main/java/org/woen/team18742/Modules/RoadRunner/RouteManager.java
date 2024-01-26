package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.code.Attribute;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.BulkInit;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Timers.ElapsedTimeExtra;
import org.woen.team18742.Tools.Vector2;

import java.util.Arrays;
import java.util.Optional;

@AutonomModule
public class RouteManager implements IRobotModule {
    private Lift _lift;
    private Intake _intake;
    private Camera _camera;
    private Drivetrain _driveTrain;
    private OdometryHandler _odometry;
    private Gyroscope _gyro;
    private Brush _brush;

    private final MecanumKinematics _mecanumKinematics = new MecanumKinematics(Configs.DriveTrainWheels.Radius * 2, 1 / Configs.Odometry.YLag);

    private final TurnConstraints _turnConstraints = new TurnConstraints(Configs.DriveTrainWheels.MaxSpeedTurn, -Configs.DriveTrainWheels.MaxTurnVelocity, Configs.DriveTrainWheels.MaxTurnVelocity);

    private final VelConstraint _velConstraint = new MinVelConstraint(Arrays.asList(_mecanumKinematics.new WheelVelConstraint(Configs.DriveTrainWheels.MaxSpeedX * Configs.Odometry.YLag), new AngularVelConstraint(Configs.DriveTrainWheels.MaxSpeedTurn)));
    private final AccelConstraint _accelConstraint = new ProfileAccelConstraint(Configs.Route.MinProfileAccel, Configs.Route.MaxProfileAccel);

    private final ElapsedTimeExtra _time = new ElapsedTimeExtra();
    private Action _trajectory;
    private boolean _isTrajectoryEnd = false, _isLiftWait = false, _isPixelWait = false;

    private final Action[][] _allTrajectory = new Action[StartRobotPosition.values().length][CameraRobotPosition.values().length];

    private static boolean _isInited = false;

    @Override
    public void Init(BaseCollector collector) {
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);
        _driveTrain = collector.GetModule(Drivetrain.class);
        _odometry = collector.GetModule(OdometryHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _brush = collector.GetModule(Brush.class);

        if (!_isInited) {
            _isInited = true;

            for (int i = 0; i < _allTrajectory.length; i++) {
                for (int j = 0; j < _allTrajectory[0].length; j++) {
                    StartRobotPosition pos = StartRobotPosition.values()[i];
                    CameraRobotPosition camPos = CameraRobotPosition.values()[j];

                    _allTrajectory[i][j] = Trajectory.GetTrajectory(ActionBuilder(
                            new Pose2d(pos.Position.X, pos.Position.Y, pos.Rotation)), camPos).build();
                }
            }
        }
    }

    @Override
    public void Start() {
        int indexStartPos = 0, indexCamera = 0;

        for (int i = 0; i < StartRobotPosition.values().length; i++)
            if (StartRobotPosition.values()[i] == Bios.GetStartPosition()) {
                indexStartPos = i;
                break;
            }

        CameraRobotPosition cameraPos = _camera.GetPosition();

        for (int i = 0; i < CameraRobotPosition.values().length; i++)
            if (CameraRobotPosition.values()[i] == cameraPos) {
                indexCamera = i;
                break;
            }

        _trajectory = _allTrajectory[indexStartPos][indexCamera];

        _time.reset();
    }

    @Override
    public void Update() {
        if (!Configs.GeneralSettings.IsAutonomEnable) return;

        if (!_isTrajectoryEnd) {
            if (_isLiftWait) {
                if (_lift.isATarget()) {
                    _isLiftWait = false;
                    _time.start();
                } else {
                    _driveTrain.Stop();
                    return;
                }
            }

            if (_isPixelWait) {
                if (_intake.isPixelGripped()) {
                    _isPixelWait = false;
                    _time.start();
                } else {
                    _driveTrain.Stop();
                    return;
                }
            }

            _isTrajectoryEnd = !_trajectory.run(new TelemetryPacket());
        } else
            _driveTrain.Stop();
    }

    private class TrajectoryAction implements Action {
        private final Optional<TimeTrajectory> _timeTrajectory;
        private final Optional<TimeTurn> _timeTurn;
        private final double _duration;

        public TrajectoryAction(TimeTurn t) {
            _timeTrajectory = Optional.empty();
            _timeTurn = Optional.of(t);
            _duration = _timeTurn.get().duration;
        }

        public TrajectoryAction(TimeTrajectory t) {
            _timeTrajectory = Optional.of(t);
            _timeTurn = Optional.empty();
            _duration = _timeTrajectory.get().duration;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            double time = _time.seconds();

            if (time >= _duration) {
                _driveTrain.Stop();
                return false;
            }

            Pose2dDual<Time> txWorldTarget = _timeTrajectory.map(trajectory -> trajectory.get(time)).orElseGet(() -> _timeTurn.get().get(time));

            Pose2d position = new Pose2d(_odometry.Position.X, _odometry.Position.Y, _gyro.GetRadians());
            PoseVelocity2d velocity = new PoseVelocity2d(new Vector2d(_odometry.Speed.X, _odometry.Speed.Y), _gyro.GetSpeedRadians());

            PoseVelocity2dDual<Time> command = new HolonomicController(Configs.PositionConnection.Axial, Configs.PositionConnection.Lateral, Configs.PositionConnection.Heading, Configs.SpeedConnection.Axial, Configs.SpeedConnection.Lateral, Configs.SpeedConnection.Heading)
                    .compute(txWorldTarget, position, velocity);

            _driveTrain.SetCMSpeed(new Vector2(command.linearVel.x.value(), command.linearVel.y.value()), command.angVel.value());

            return true;
        }

        @Override
        public void preview(@NonNull Canvas fieldOverlay) {
        }
    }

    private MyTrajectoryBuilder ActionBuilder(Pose2d beginPose) {
        return new MyTrajectoryBuilder(new TrajectoryActionBuilder(TrajectoryAction::new, TrajectoryAction::new, beginPose, 1e-6, 0.0, _turnConstraints, _velConstraint, _accelConstraint, 0.25, 0.1));
    }

    public final class MyTrajectoryBuilder {
        private final TrajectoryActionBuilder _builder;

        public MyTrajectoryBuilder(TrajectoryActionBuilder builder) {
            _builder = builder;
        }

        public Action build() {
            return _builder.build();
        }

        public MyTrajectoryBuilder splineTo(Vector2d vec, double tangent) {
            return new MyTrajectoryBuilder(_builder.splineTo(vec, tangent));
        }

        public MyTrajectoryBuilder splineToConstantHeading(Vector2d vec, double tangent) {
            return new MyTrajectoryBuilder(_builder.splineToConstantHeading(vec, tangent));
        }

        public MyTrajectoryBuilder setReversed(boolean reversed) {
            return new MyTrajectoryBuilder(_builder.setReversed(reversed));
        }

        public MyTrajectoryBuilder liftUp() {
            return liftUp(0);
        }

        public MyTrajectoryBuilder liftUp(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _lift.SetLiftPose(LiftPose.UP)));
        }

        public MyTrajectoryBuilder liftDown() {
            return liftDown(0);
        }

        public MyTrajectoryBuilder liftDown(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _lift.SetLiftPose(LiftPose.DOWN)));
        }

        public MyTrajectoryBuilder waitLift() {
            return new MyTrajectoryBuilder(_builder.stopAndAdd(() -> {
                _time.pause();
                _isLiftWait = true;
            }));
        }

        public MyTrajectoryBuilder brushOn() {
            return brushOn(0);
        }

        public MyTrajectoryBuilder brushOn(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _brush.BrushEnable()));
        }

        public MyTrajectoryBuilder waitPixel() {
            return new MyTrajectoryBuilder(_builder.stopAndAdd(() -> {
                _time.pause();
                _isPixelWait = true;
            }));
        }

        public MyTrajectoryBuilder pixelDeGripped() {
            return pixelDeGripped(0);
        }

        public MyTrajectoryBuilder pixelDeGripped(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _intake.setGripper(false)));
        }

        public MyTrajectoryBuilder turnTo(double heading) {
            return new MyTrajectoryBuilder(_builder.turnTo(heading));
        }

        public MyTrajectoryBuilder waitSeconds(double time) {
            return new MyTrajectoryBuilder(_builder.waitSeconds(time));
        }
    }
}
/*
package org.woen.team18742.Modules;

import static java.lang.Math.PI;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;
import java.util.List;

@AutonomModule
public class RouteManager implements IRobotModule {
    private final List<Runnable> _route = new ArrayList<>();

    private int _currentRouteAction = 0;
    private double _waitTime = -1;
    private boolean _isPixelWait = false;

    private Automatic _automatic;
    private Lift _lift;
    private Intake _intake;
    private AutonomCollector _collector;
    private Camera _camera;
    private ElapsedTime _timer = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _automatic = collector.GetModule(Automatic.class);
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);

        if (collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    private void Wait(double sleep) {
        _timer.reset();
        _waitTime = sleep;
    }

    @Override
    public void Start() {
        if(_collector == null)
            return;

        _intake.setGripper(true);

        _timer.reset();

        switch (AutonomCollector.StartPosition) {
            case RED_BACK: {
                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-48, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, 30)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-65, 0)));
                        break;
                    }

                    case RIGHT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-39, 29), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(15, -40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(5, 0)));
                        _route.add(() -> _automatic.PIDMove(new Vector2(-80, 5)));
                        break;
                    }

                    case LEFT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-20, 5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-10, -18)));
                        _route.add(()->_automatic.PIDMove(new Vector2(10, 18)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-50, 5)));
                        break;
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-130, -130), PI / 2));

                switch (_camera.GetPosition()) {
                    case FORWARD:
                        _route.add(() -> _automatic.PIDMove(new Vector2(76, -98)));
                        break;

                    case RIGHT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(53, -102)));
                        break;

                    case LEFT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(86, -98)));
                        break;
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-123, -215), PI / 2));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, -30)));

                break;
            }

            case BLUE_BACK: {
                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-54, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, -40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-45, 0)));
                        break;
                    }

                    case RIGHT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-39, -29), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(15, 40)));
                        _route.add(()->_automatic.PIDMove(new Vector2(5, 0)));
                        _route.add(() -> _automatic.PIDMove(new Vector2(-80, -5)));
                        break;
                    }

                    case LEFT:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-20, -9)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-10, 22)));
                        _route.add(()->_automatic.PIDMove(new Vector2(10, -22)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-60, -5)));
                        break;
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-125, 130), -PI / 2));

                _route.add(()->_automatic.SetSpeed(0.3));

                switch (_camera.GetPosition()) {
                    case FORWARD:
                        _route.add(() -> _automatic.PIDMove(new Vector2(59, 98)));
                        break;

                    case RIGHT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(39, 102)));
                        break;

                    case LEFT:
                        _route.add(() -> _automatic.PIDMove(new Vector2(78, 98)));
                        break;
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.SetSpeed(0.5));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-135, 215), -PI / 2));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, 30)));

                break;
            }

            case RED_FORWARD:{
                _automatic.SetSpeed(0.4);

                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-62, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(30, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, -10)));
                        break;
                    }

                    case LEFT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-55, -22), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(20, -20), 0));
                        break;
                    }

                    case RIGHT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, -5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-12, 15)));
                        _route.add(()->_automatic.PIDMove(new Vector2(12, 0)));
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-65, -92), PI / 2));

                switch (_camera.GetPosition()){
                    default:
                        break;

                    case LEFT:
                        _route.add(()->_automatic.PIDMove(new Vector2(40, 0)));

                    case RIGHT:
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 0)));
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-10, -76)));
                _route.add(() -> _automatic.PIDMove(new Vector2(0, -20)));

                break;
            }

            default:{
                _automatic.SetSpeed(0.4);

                switch (_camera.GetPosition()){
                    case FORWARD:
                    {
                        _route.add(()->_automatic.PIDMove(new Vector2(-62, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(30, 0), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(0, 10)));
                        break;
                    }

                    case LEFT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-55, 22), 0));
                        _route.add(()->_automatic.PIDMove(new Vector2(20, 20), 0));
                        break;
                    }

                    case RIGHT:{
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 5)));
                        _route.add(()->_automatic.PIDMove(new Vector2(-30, 0), -PI / 2));
                        _route.add(()->_automatic.PIDMove(new Vector2(-12, -15)));
                        _route.add(()->_automatic.PIDMove(new Vector2(12, 0)));
                    }
                }

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-65, 92), PI / 2));

                switch (_camera.GetPosition()){
                    default:
                        break;

                    case LEFT:
                        _route.add(()->_automatic.PIDMove(new Vector2(40, 0)));

                    case RIGHT:
                        _route.add(()->_automatic.PIDMove(new Vector2(-25, 0)));
                }

                _route.add(() -> _lift.SetLiftPose(LiftPose.AVERAGE));
                _route.add(() -> Wait(1500));
                _route.add(() -> _intake.releaseGripper());
                _route.add(() -> Wait(500));
                _route.add(() -> _lift.SetLiftPose(LiftPose.DOWN));

                _route.add(()->_automatic.PIDMoveToPoint(new Vector2(-10, 76)));
                _route.add(() -> _automatic.PIDMove(new Vector2(0,  20)));

                break;
            }
        }
    }

    @Override
    public void LastUpdate() {
        if (Configs.GeneralSettings.IsAutonomEnable) {
            if (_automatic.isMovedEnd() && _lift.isATarget() && (!_isPixelWait || _intake.isPixelGripped()) && _timer.milliseconds() > _waitTime) {
                if (_currentRouteAction < _route.size()) {
                    _isPixelWait = false;
                    _route.get(_currentRouteAction).run();

                    _currentRouteAction++;
                }
            }
        }
    }
}

 */