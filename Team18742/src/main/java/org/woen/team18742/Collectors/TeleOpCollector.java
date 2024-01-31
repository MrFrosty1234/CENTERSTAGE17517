package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Manager.TeleopModule;

import java.util.ArrayList;

public class TeleOpCollector extends BaseCollector {
    private static ArrayList<Class<?>> _annotatedClass;

    public TeleOpCollector(LinearOpMode robot) {
        super(robot);

        if(_annotatedClass == null)
            _annotatedClass = GetAnnotatedClasses(TeleopModule.class);

        AddAdditionModules(_annotatedClass);

        Init();
    }
}