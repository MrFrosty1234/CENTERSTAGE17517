package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Modules.Manager.BulkInit;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.Motor.MotorsHandler;
import org.woen.team18742.Tools.Timers.TimerHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexFile;

public class BaseCollector {
    public LinearOpMode Robot;
    public ElapsedTime Time;
    private final Battery _battery;

    private final ArrayList<IRobotModule> _modules = new ArrayList<>();

    private static ArrayList<Class<?>> _annotatedClass;

    private TimerHandler _timers;
    private MotorsHandler _motors;

    public BaseCollector(LinearOpMode robot) {
        Robot = robot;

        _timers = new TimerHandler();
        _motors = new MotorsHandler();

        _modules.clear();

        Devices.Init(robot.hardwareMap);

        _battery = new Battery(this);
        Time = new ElapsedTime();

        if(_annotatedClass == null)
            _annotatedClass = GetAnnotatedClasses(Module.class);

        AddAdditionModules(_annotatedClass);
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

            for(Class<?> i: _classes){
                for(Method j: i.getMethods())
                    if(j.isAnnotationPresent(BulkInit.class)) {
                        try {
                            j.invoke(null);
                        }
                        catch (Exception e){
                            throw new RuntimeException("failed to invoke method: " + j.getName());
                        }
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

        _motors.Start();

        for (IRobotModule i : _modules)
            i.Start();
    }

    public void Update() {
        _motors.Update();
        _battery.Update();

        for (IRobotModule i : _modules)
            i.Update();

        for (IRobotModule i : _modules)
            i.LastUpdate();

        _timers.Update();
    }

    public void Stop() {
        for (IRobotModule i : _modules)
            i.Stop();
    }

    public void Init(){
        for (IRobotModule i : _modules)
            i.Init(this);
    }

    protected void AddAdditionModules(ArrayList<Class<?>> modules) {
        for (Class<?> i : modules) {
            Object instance;

            try {
                instance = i.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("not correct constructor in module " + i.getName());
            }

            if (instance instanceof IRobotModule)
                _modules.add((IRobotModule) instance);
        }
    }

    public <T extends IRobotModule> T GetModule(Class<T> type) {
        for (IRobotModule i : _modules)
            if (i.getClass() == type)
                return (T) i;

        throw new RuntimeException("not found " + type.getName() + " module");
    }
}
