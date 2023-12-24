package org.woen.team18742.Collectors;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;
import java.util.List;

@Config
public class AutonomCollector extends BaseCollector {
    public static boolean IsAutonomEnable = true;
    public Automatic Auto;
    public org.woen.team18742.Modules.Odometry.Odometry Odometry;
    public Camera Camera;
    private final VisionPortalHandler _visionHandler;

    private List<Runnable> _route = new ArrayList<>();

    private int _currentRouteAction = 0;
    private boolean _isPixelWait = false;

    private StartRobotPosition _startPosition = StartRobotPosition.BLUE_BACK;

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

        Auto.Start(_startPosition.Position);
        Odometry.Start(_startPosition.Position);

        //Intake.PixelCenterGrip(true);

        switch (Camera.GetPosition()) {
            case FORWARD: {
                _route.add(()-> Auto.PIDMove(new Vector2(-54, 0)));

                break;
            }

            case RIGHT: {
                _route.add(()->Auto.PIDMove(new Vector2(-54, 30)));

                break;
            }

            default: {
                _route.add(()->Auto.PIDMove(new Vector2(-54, -30)));

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
                if (_currentRouteAction < _route.size()) {
                    _isPixelWait = false;
                    _route.get(_currentRouteAction).run();

                    _currentRouteAction++;
                }
            }
        }
    }

    public void PreUpdate(){
        if(Robot.gamepad1.dpad_left)
            _startPosition = StartRobotPosition.BLUE_BACK;
        else if(Robot.gamepad1.dpad_right)
            _startPosition = StartRobotPosition.BLUE_FORWAD;
        else if(Robot.gamepad1.dpad_up)
            _startPosition = StartRobotPosition.RED_BACK;
        else if(Robot.gamepad1.dpad_down)
            _startPosition = StartRobotPosition.RED_FORWARD;
    }

    @Override
    public void Stop() {
        _visionHandler.Stop();
    }
}