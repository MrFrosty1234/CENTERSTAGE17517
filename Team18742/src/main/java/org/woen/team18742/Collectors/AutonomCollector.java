package org.woen.team18742.Collectors;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Config
public class AutonomCollector extends BaseCollector {
    public static boolean IsAutonomEnable = true;
    public Automatic Auto;
    public org.woen.team18742.Modules.Odometry.Odometry Odometry;
    public Camera Camera;
    private final VisionPortalHandler _visionHandler;

    private Runnable[] _route;

    private int _currentRouteAction = 0;
    private boolean _isPixelWait = false;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Camera = new Camera();
        Odometry = new Odometry(this);
        Auto = new Automatic(this);

        CameraStreamSource v = Camera.GetProcessor();

        _visionHandler = new VisionPortalHandler(new VisionProcessor[]{Odometry.GetProcessor(), (VisionProcessor) v});

        _visionHandler.StartDashBoardVid(v);
    }

    @Override
    public void Start() {
        super.Start();

        Odometry.Start();

        switch (Camera.GetPosition()) {
            case FORWARD: {
                _route = new Runnable[]{
                        () -> Auto.TurnGyro(0)
                };

                break;
            }

            case RIGHT: {
                _route = new Runnable[]{
                        () -> Auto.PIDMove(new Vector2(80, 5)),
                        () -> Auto.PIDMove(new Vector2(-10, 60))
                };

                break;
            }

            default: {
                _route = new Runnable[]{
                        () -> Auto.PIDMove(new Vector2(60, 0)),
                        () -> Auto.PIDMove(new Vector2(-10, 60))
                };
                break;
            }
        }
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();

        ToolTelemetry.AddLine("camera = " + Camera.GetPosition());

        if (IsAutonomEnable) {
            Auto.Update();

            if (Auto.isMovedEnd() && Lift.isATarget() && (!_isPixelWait || Intake.isPixelLocated)) {
                if (_currentRouteAction < _route.length) {
                    _isPixelWait = false;
                    _route[_currentRouteAction].run();

                    _currentRouteAction++;
                }
            }
        }
    }

    @Override
    public void Stop() {
        _visionHandler.Stop();
    }
}