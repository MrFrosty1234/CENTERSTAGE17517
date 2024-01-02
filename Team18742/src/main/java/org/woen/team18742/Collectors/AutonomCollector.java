package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Camera.VisionPortalHandler;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Configs.Configs;

public class AutonomCollector extends BaseCollector {
    public StartRobotPosition StartPosition = StartRobotPosition.RED_BACK;

    public AutonomCollector(LinearOpMode robot) {
        super(robot);

        AddAdditionModules(BaseCollector.GetAnnotatedClasses(AutonomModule.class));
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