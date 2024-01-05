package org.woen.team18742.Modules;

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

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Vector2;

import java.util.Arrays;
import java.util.Optional;

@AutonomModule
public class RouteManager implements IRobotModule {
    private Lift _lift;
    private Intake _intake;
    private AutonomCollector _collector;
    private Camera _camera;
    private Drivetrain _driveTrain;
    private Odometry _odometry;
    private Gyroscope _gyro;
    private Brush _brush;

    private final MecanumKinematics _mecanumKinematics = new MecanumKinematics(Configs.Route.TrackWidth, 1 / Configs.Odometry.LateralMultiplier);

    private final TurnConstraints _turnConstraints = new TurnConstraints(Configs.DriveTrainWheels.MaxSpeedTurn, -Configs.DriveTrainWheels.MaxTurnVelocity, Configs.DriveTrainWheels.MaxTurnVelocity);

    private final VelConstraint _velConstraint = new MinVelConstraint(Arrays.asList(_mecanumKinematics.new WheelVelConstraint(Configs.DriveTrainWheels.MaxSpeedX), new AngularVelConstraint(Configs.DriveTrainWheels.MaxSpeedTurn)));
    private final AccelConstraint _accelConstraint = new ProfileAccelConstraint(Configs.Route.MinProfileAccel, Configs.Route.MaxProfileAccel);

    private ElapsedTime _time;
    private Action _trajectory;
    private boolean _isTrajectoryEnd = false, _isLiftWait = false, _isPixelWait = false;

    @Override
    public void Init(BaseCollector collector) {
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);
        _driveTrain = collector.GetModule(Drivetrain.class);
        _odometry = collector.GetModule(Odometry.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _brush = collector.GetModule(Brush.class);

        _time = collector.Time;

        if (collector instanceof AutonomCollector) _collector = (AutonomCollector) collector;
    }

    @Override
    public void Start() {
        if (_collector == null) return;

        _trajectory = GetTrajectory(ActionBuilder(new Pose2d(_collector.StartPosition.Position.X, _collector.StartPosition.Position.Y, 0))).build();
    }

    private MyTrajectoryBuilder GetTrajectory(MyTrajectoryBuilder builder) {
        switch (_camera.GetPosition()) {
            case LEFT:
                return builder
                        .liftUp()
                        .splineTo(new Vector2d(0, 0), 0)
                        .splineTo(new Vector2d(10, 10), 0)
                        .waitLift();

            case RIGHT:
                return builder
                        .waitPixel()
                        .liftUp()
                        .splineTo(new Vector2d(20, 20), 0)
                        .waitLift()
                        .pixelDeGripped();

            case FORWARD:
                return builder;
        }

        throw new RuntimeException("not successful get team element position");
    }

    @Override
    public void Update() {
        if (!Configs.GeneralSettings.IsAutonomEnable) return;

        if (!_isTrajectoryEnd) {
            if (_isLiftWait) {
                if (_lift.isATarget()) _isLiftWait = false;
                else return;
            }

            if (_isPixelWait) {
                if (_intake.isPixelGripped()) _isPixelWait = false;
                else return;
            }

            _isTrajectoryEnd = !_trajectory.run(new TelemetryPacket());
        }
    }

    private final class TrajectoryAction implements Action {
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
            PoseVelocity2d velocity = new PoseVelocity2d(new Vector2d(_odometry.Speed.X, _odometry.Speed.Y), _gyro.SpeedTurn);

            PoseVelocity2dDual<Time> command = new HolonomicController(Configs.PositionConnection.Axial, Configs.PositionConnection.Lateral, Configs.PositionConnection.Heading, Configs.SpeedConnection.Axial, Configs.SpeedConnection.Lateral, Configs.SpeedConnection.Heading).compute(txWorldTarget, position, velocity);

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

    private final class MyTrajectoryBuilder {
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
            return waitLift(0);
        }

        public MyTrajectoryBuilder waitLift(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _isLiftWait = true));
        }

        public MyTrajectoryBuilder brushOn() {
            return brushOn(0);
        }

        public MyTrajectoryBuilder brushOn(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _brush.IntakePowerWithProtection()));
        }

        public MyTrajectoryBuilder waitPixel() {
            return waitPixel(0);
        }

        public MyTrajectoryBuilder waitPixel(double ds) {
            return new MyTrajectoryBuilder(_builder.afterDisp(ds, () -> _isPixelWait = true));
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