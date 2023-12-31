package org.woen.team18742.Collectors;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;
import java.util.List;

public class AutonomCollector extends BaseCollector {
    public StartRobotPosition StartPosition = StartRobotPosition.RED_BACK;

    public AutonomCollector(LinearOpMode robot) {
        super(robot);

        AddAdditionModules(AutonomModule.class.getClasses());
    }

    public void PreUpdate(){
        if(Robot.gamepad1.dpad_left)
            StartPosition = StartRobotPosition.BLUE_BACK;
        else if(Robot.gamepad1.dpad_right)
            StartPosition = StartRobotPosition.BLUE_FORWAD;
        else if(Robot.gamepad1.dpad_up)
            StartPosition = StartRobotPosition.RED_BACK;
        else if(Robot.gamepad1.dpad_down)
            StartPosition = StartRobotPosition.RED_FORWARD;
    }
}