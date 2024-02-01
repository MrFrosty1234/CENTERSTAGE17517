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
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Modules.PidRunner.PidRouteManager;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Timers.ElapsedTimeExtra;
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

    private final MecanumKinematics _mecanumKinematics = new MecanumKinematics(Configs.DriveTrainWheels.Radius * 2, 1 / Configs.Odometry.YLag);

    private final TurnConstraints _turnConstraints = new TurnConstraints(Configs.DriveTrainWheels.MaxSpeedTurn, -Configs.DriveTrainWheels.MaxTurnAccel, Configs.DriveTrainWheels.MaxTurnAccel);

    private final VelConstraint _velConstraint = new MinVelConstraint(Arrays.asList(_mecanumKinematics.new WheelVelConstraint(Configs.DriveTrainWheels.MaxSpeedX), new AngularVelConstraint(Configs.DriveTrainWheels.MaxSpeedTurn)));
    private final AccelConstraint _accelConstraint = new ProfileAccelConstraint(Configs.Route.MinProfileAccel, Configs.Route.MaxProfileAccel);

    private static final ElapsedTimeExtra _time = new ElapsedTimeExtra();
    private Action _trajectory;
    private boolean _isTrajectoryEnd = false;

    private List<BooleanSupplier> _waiters = new ArrayList<>();

    private static final Action[][] _allTrajectory = new Action[StartRobotPosition.values().length][CameraRobotPosition.values().length];

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
        _automaticPid = collector.GetModule(PidRouteManager.class);

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

        _trajectory = Trajectory.GetTrajectory(ActionBuilder(
                new Pose2d(pos.Position.X, pos.Position.Y, pos.Rotation)), _camera.GetPosition()).build();//_allTrajectory[indexStartPos][indexCamera];

        _time.reset();
    }

    @Override
    public void Update() {
        if (!Configs.GeneralSettings.IsAutonomEnable) return;

        if (!_isTrajectoryEnd) {
            if (_waiters.size() > 0){
                _driveTrain.Stop();

                for(BooleanSupplier i : _waiters){
                    if(i.getAsBoolean())
                        _waiters.remove(i);
                }

                return;
            }
            else
                _time.start();

            _isTrajectoryEnd = !_trajectory.run(new TelemetryPacket());
        } else
            _driveTrain.Stop();
    }

    private class TrajectoryAction implements Action {
        private final Optional<TimeTrajectory> _timeTrajectory;
        private final Optional<TimeTurn> _timeTurn;
        private final double _duration;
        private ElapsedTime trajectoryTimer = null;

       //private double xPoints[], yPoints[];

        public TrajectoryAction(TimeTurn t) {
            _timeTrajectory = Optional.empty();
            _timeTurn = Optional.of(t);
            _duration = _timeTurn.get().duration;
        }

        public TrajectoryAction(TimeTrajectory timeTrajectory) {
            _timeTrajectory = Optional.of(timeTrajectory);
            _timeTurn = Optional.empty();
            _duration = _timeTrajectory.get().duration;

            /*List<Double> disps = Math.range(0, timeTrajectory.path.length(), Math.max(2, Math.ceil(timeTrajectory.path.length() / 2)));

            xPoints = new double[disps.size()];
            yPoints = new double[disps.size()];

            for(int i = 0; i < disps.size(); i++){
                Pose2d pose = timeTrajectory.path.get(disps.get(i), 1).value();

                xPoints[i] = pose.position.x;
                yPoints[i] = pose.position.y;
            }*/
        }

        @Override
        public boolean run(@NonNull TelemetryPacket p) {
            if(trajectoryTimer == null){
                trajectoryTimer = new ElapsedTime();
                trajectoryTimer.reset();
            }

            double time = trajectoryTimer.seconds();//_time.seconds();

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

//            ToolTelemetry.GetCanvas().setStroke("#4CAF50FF");
  //          ToolTelemetry.GetCanvas().setStrokeWidth(1);
    //        ToolTelemetry.GetCanvas().strokePolyline(xPoints, yPoints);

            return true;
        }

        @Override
        public void preview(@NonNull Canvas fieldOverlay) {
            //ToolTelemetry.GetCanvas().setStroke("#4CAF507A");
            //ToolTelemetry.GetCanvas().setStrokeWidth(1);
            //ToolTelemetry.GetCanvas().strokePolyline(xPoints, yPoints);
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
                _waiters.add(()->_lift.isATarget());
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
                _waiters.add(()->_intake.isPixelGripped());
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

        public MyTrajectoryBuilder PidMove(ArrayList<Runnable> route){
            return new MyTrajectoryBuilder(_builder.stopAndAdd(()->{
                _automaticPid.Start(route);
                _waiters.add(()->_automaticPid.IsEnd());
                _time.pause();
            }));
        }

        public MyTrajectoryBuilder strafeTo(Vector2d pos){
            return new MyTrajectoryBuilder(_builder.strafeTo(pos));
        }
    }
}