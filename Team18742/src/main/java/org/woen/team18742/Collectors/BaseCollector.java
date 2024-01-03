package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Modules.Manager.RobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.ToolTelemetry;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexFile;

public class BaseCollector {
    public LinearOpMode Robot;
    public ElapsedTime Time;
    private final Battery _battery;

    private final ArrayList<RobotModule> _modules = new ArrayList<>();

    private static ArrayList<Class<?>> _annotatedClass;

    public BaseCollector(LinearOpMode robot) {
        Robot = robot;

        //Devices.Init(robot.hardwareMap);
        ToolTelemetry.SetTelemetry(Robot.telemetry);

        _battery = new Battery(this);
        Time = new ElapsedTime();

        if(_annotatedClass == null)
            _annotatedClass = GetAnnotatedClasses(Module.class);

        AddAdditionModules(_annotatedClass);

        ToolTelemetry.Update();
    }

    private static ArrayList<Class<?>> _classes;

    public static ArrayList<Class<?>> GetAnnotatedClasses(Class<? extends Annotation> annotation) {
        if (_classes == null) {
            List<String> classNames;

            try {
                DexFile dexFile = new DexFile(AppUtil.getInstance().getActivity().getPackageCodePath());

                classNames = Collections.list(dexFile.entries());
            } catch (Exception e) {
                throw new RuntimeException("not successful search file names");
            }

            _classes = new ArrayList<>();

            for (String i : classNames) {
                if (!i.contains("team18742"))
                    continue;

                try {
                    _classes.add(Class.forName(i));
                } catch (Exception e) {
                    throw new RuntimeException("not successful find " + i + " class");
                }
            }
        }

        ArrayList<Class<?>> result = new ArrayList<>();

        for (Class<?> i : _classes)
            if (i.isAnnotationPresent(annotation))
                result.add(i);

        return result;
    }

    public void Start() {
        Time.reset();

        for (RobotModule i : _modules)
            i.Start();
    }

    public void Update() {
        _battery.Update();

        for (RobotModule i : _modules)
            i.Update();

        ToolTelemetry.Update();
    }

    public void Stop() {
        for (RobotModule i : _modules)
            i.Stop();
    }

    protected void AddAdditionModules(ArrayList<Class<?>> modules) {
        for (Class<?> i : modules) {
            Object instance;

            try {
                instance = i.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("not correct constructor in module " + i.getName());
            }

            if (instance instanceof RobotModule)
                _modules.add((RobotModule) instance);
        }

        ToolTelemetry.AddLine("activated modules = " + _modules.size());

        for (RobotModule i : _modules)
            i.Init(this);
    }

    public <T extends RobotModule> T GetModule(Class<T> type) {
        for (RobotModule i : _modules)
            if (i.getClass() == type)
                return (T) i;

        throw new RuntimeException("not found " + type.getName() + "module");
    }
}
