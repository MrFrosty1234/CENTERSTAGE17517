package org.woen.team18742.Modules.RoadRunner;

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

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Brush.Brush;
import org.woen.team18742.Modules.Brush.StaksBrush;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Modules.PidRunner.PidRouteManager;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Timers.ElapsedTimeExtra;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@AutonomModule
public class RoadRunnerRouteManager implements IRobotModule {
    private Lift _lift;
    private Intake _intake;
    private Camera _camera;
    private Drivetrain _driveTrain;
    private OdometryHandler _odometry;
    private Gyroscope _gyro;
    private Brush _brush;
    private PidRouteManager _automaticPid;
    private StaksBrush _staksBrush;

    private final MecanumKinematics _mecanumKinematics = new MecanumKinematics(Configs.DriveTrainWheels.Radius * 2 + 5, 1 / Configs.Odometry.YLag);

    private final TurnConstraints _turnConstraints = new TurnConstraints(Configs.DriveTrainWheels.MaxSpeedTurn, -Configs.DriveTrainWheels.MaxTurnAccel, Configs.DriveTrainWheels.MaxTurnAccel);

    private final VelConstraint _velConstraint = new MinVelConstraint(Arrays.asList(_mecanumKinematics.new WheelVelConstraint(Configs.DriveTrainWheels.MaxSpeedX), new AngularVelConstraint(Configs.DriveTrainWheels.MaxSpeedTurn)));
    private final AccelConstraint _accelConstraint = new ProfileAccelConstraint(Configs.Route.MinProfileAccel, Configs.Route.MaxProfileAccel);

    private List<Action> _trajectorys;
    private boolean _isTrajectoryEnd = false;

    private BooleanSupplier _waiter = ()->true;
    private boolean _isWait = false;

    private static final Action[][] _allTrajectory = new Action[StartRobotPosition.values().length][CameraRobotPosition.values().length];

    private static boolean _isInited = false;

    private int _currentTrajectory = 0;

    @Override
    public void Init(BaseCollector collector) {
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _camera = collector.GetModule(Camera.class);
        _driveTrain = collector.GetModule(Drivetrain.class);
        _odometry = collector.GetModule(OdometryHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _brush = collector.GetModule(Brush.class);
        _automaticPid = collector.GetModule(PidRouteManager.class);
        _staksBrush = collector.GetModule(StaksBrush.class);

        /*if (!_isInited) {
            _isInited = true;

            for (int i = 0; i < _allTrajectory.length; i++) {
                for (int j = 0; j < _allTrajectory[0].length; j++) {
                    StartRobotPosition pos = StartRobotPosition.values()[i];
                    CameraRobotPosition camPos = CameraRobotPosition.values()[j];

                    _allTrajectory[i][j] = Trajectory.GetTrajectory(ActionBuilder(
                            new Pose2d(pos.Position.X, pos.Position.Y, pos.Rotation)), camPos).build();
                }
            }
        }*/
    }

    @Override
    public void Start() {
        /*int indexStartPos = 0, indexCamera = 0;

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
            }*/

        StartRobotPosition pos = Bios.GetStartPosition();

        _trajectorys = Trajectory.GetTrajectory(ActionBuilder(
                new Pose2d(pos.Position.X, pos.Position.Y, pos.Rotation)), _camera.GetPosition()).build();//_allTrajectory[indexStartPos][indexCamera];

        //_intake.PixelCenterGrip(true);

        for(Action i : _trajectorys)
            i.preview(new Canvas());
    }

    @Override
    public void Update() {
        if (!Configs.GeneralSettings.IsAutonomEnable) return;

        if (!_isTrajectoryEnd) {
            if(_isWait)
                if(_waiter.getAsBoolean()) {
                    _currentTrajectory++;
                    _isWait = false;
                }

            if(!_trajectorys.get(_currentTrajectory).run(new TelemetryPacket())){
                if(_currentTrajectory + 1 < _trajectorys.size())
                    _isWait = true;
                else
                    _isTrajectoryEnd = true;
            }
        } else
            _driveTrain.Stop();
    }

    private class TrajectoryAction implements Action {
        private final Optional<TimeTrajectory> _timeTrajectory;
        private final Optional<TimeTurn> _timeTurn;
        private final double _duration;
        private ElapsedTimeExtra trajectoryTimer = null;

        private double xPoints[], yPoints[];

        public TrajectoryAction(TimeTurn t) {
            _timeTrajectory = Optional.empty();
            _timeTurn = Optional.of(t);
            _duration = _timeTurn.get().duration;
        }

        public TrajectoryAction(TimeTrajectory timeTrajectory) {
            _timeTrajectory = Optional.of(timeTrajectory);
            _timeTurn = Optional.empty();
            _duration = _timeTrajectory.get().duration;

            List<Double> disps = com.acmerobotics.roadrunner.Math.range(0, timeTrajectory.path.length(), (int) Math.max(2d, Math.ceil(timeTrajectory.path.length() / 2)));

            xPoints = new double[disps.size()];
            yPoints = new double[disps.size()];

            for (int i = 0; i < disps.size(); i++) {
                Pose2d pose = timeTrajectory.path.get(disps.get(i), 1).value();

                xPoints[i] = DistanceUnit.INCH.fromCm(pose.position.x);
                yPoints[i] = DistanceUnit.INCH.fromCm(pose.position.y);
            }
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            if (trajectoryTimer == null) {
                trajectoryTimer = new ElapsedTimeExtra();
                trajectoryTimer.reset();
            }

            double time = trajectoryTimer.seconds();

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

            ToolTelemetry.GetCanvas().setStroke("#4CAF50FF");
            ToolTelemetry.GetCanvas().setStrokeWidth(1);
            ToolTelemetry.GetCanvas().strokePolyline(xPoints, yPoints);

            return true;
        }

        @Override
        public void preview(@NonNull Canvas fieldOverlay) {
            ToolTelemetry.GetCanvas().setStroke("#4CAF507A");
            ToolTelemetry.GetCanvas().setStrokeWidth(1);
            ToolTelemetry.GetCanvas().strokePolyline(xPoints, yPoints);
        }

        public void Pause() {
            trajectoryTimer.pause();
        }

        public void Start() {
            trajectoryTimer.start();
        }
    }

    private MyTrajectoryBuilder ActionBuilder(Pose2d beginPose) {
        return new MyTrajectoryBuilder(new TrajectoryActionBuilder(TrajectoryAction::new, TrajectoryAction::new, beginPose, 1e-6, 0.0, _turnConstraints, _velConstraint, _accelConstraint, 0.25, 0.1));
    }

    public final class MyTrajectoryBuilder {
        private List<TrajectoryActionBuilder> _builders = new ArrayList<>();

        public MyTrajectoryBuilder(TrajectoryActionBuilder builder) {
            _builders.add(builder);
        }

        public List<Action> build() {
            List<Action> result = new ArrayList<>();

            for(TrajectoryActionBuilder i : _builders)
                result.add(i.build());

            return result;
        }

        public MyTrajectoryBuilder splineTo(Vector2d vec, double tangent) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).splineTo(vec, tangent));
            return this;
        }

        public MyTrajectoryBuilder splineToConstantHeading(Vector2d vec, double tangent) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).splineToConstantHeading(vec, tangent));
            return this;
        }

        public MyTrajectoryBuilder setReversed(boolean reversed) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).setReversed(reversed));
            return this;
        }

        public MyTrajectoryBuilder liftUp() {
            return liftUp(0);
        }

        public MyTrajectoryBuilder liftUp(double ds) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).afterTime(ds, () -> _lift.SetLiftPose(LiftPose.MIDDLE_UPPER)));
            return this;
        }

        public MyTrajectoryBuilder liftDown() {
            return liftDown(0);
        }

        public MyTrajectoryBuilder liftDown(double ds) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).afterTime(ds, () -> _lift.SetLiftPose(LiftPose.DOWN)));
            return this;
        }

        /*public MyTrajectoryBuilder waitLift() {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).stopAndAdd(() -> {
                // _trajectory.Pause();
                _waiters.add(() -> _lift.isATarget());
            }));
            return this;
        }*/

        public MyTrajectoryBuilder brushOn() {
            return brushOn(0);
        }

        public MyTrajectoryBuilder brushOn(double ds) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).afterTime(ds, () -> _brush.BrushEnable()));
            return this;
        }

        /*public MyTrajectoryBuilder waitPixel() {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).stopAndAdd(() -> {
                // _trajectory.Pause();
                _waiters.add(() -> _intake.isPixelGripped());
            }));

            return this;
        }*/

        public MyTrajectoryBuilder pixelDeGripp() {
            return pixelDeGripp(0);
        }

        public MyTrajectoryBuilder pixelDeGripp(double ds) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).afterTime(ds, () -> _intake.releaseGripper()));
            return this;
        }

        public MyTrajectoryBuilder turnTo(double heading) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).turnTo(heading));
            return this;
        }

        public MyTrajectoryBuilder waitSeconds(double time) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).waitSeconds(time));
            return this;
        }

        public MyTrajectoryBuilder PidMove(ArrayList<Runnable> route) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).stopAndAdd(() -> {
                _automaticPid.Start(route);
            }));

            return this;
        }

        public MyTrajectoryBuilder strafeTo(Vector2d pos) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).strafeTo(pos));
            return this;
        }

        public MyTrajectoryBuilder lineToX(double pos) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).lineToX(pos));
            return this;
        }

        public MyTrajectoryBuilder lineToY(double pos) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).lineToY(pos));
            return this;
        }

        public MyTrajectoryBuilder strafeToLinearHeading(Vector2d vec, double heading) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).strafeToLinearHeading(vec, heading));
            return this;
        }

        public MyTrajectoryBuilder brushDown() {
            return brushDown(0);
        }

        public MyTrajectoryBuilder brushDown(double ds) {
            _builders.set(_builders.size() - 1, _builders.get(_builders.size() - 1).afterTime(ds, () ->_staksBrush.servoSetDownPose() ));
            return this;
        }
    }
}