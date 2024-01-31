package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.StartRobotPosition;

import java.util.ArrayList;

public class AutonomCollector extends BaseCollector {
    private static ArrayList<Class<?>> _annotatedClass;

    public AutonomCollector(LinearOpMode robot) {
        super(robot);

        if(_annotatedClass == null)
            _annotatedClass = GetAnnotatedClasses(AutonomModule.class);

        AddAdditionModules(_annotatedClass);

        Init();
    }
}