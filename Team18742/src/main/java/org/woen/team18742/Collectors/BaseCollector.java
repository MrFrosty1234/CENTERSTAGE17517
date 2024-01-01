package org.woen.team18742.Collectors;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Modules.Brush;
import org.woen.team18742.Modules.Cachinger;
import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.OdometrsHandler;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseCollector {
    public LinearOpMode Robot;
    public ElapsedTime Time;
    private Battery _battery;

    private ArrayList<IRobotModule> _modules = new ArrayList<>();

    public BaseCollector(LinearOpMode robot) {
        Robot = robot;

        Devices.Init(robot.hardwareMap);
        ToolTelemetry.SetTelemetry(Robot.telemetry);

        _battery = new Battery(this);
        Time = new ElapsedTime();

        AddAdditionModules(AnnotationFinder.GetAnnotation(Module.class));
    }

    public void Start(){
        Time.reset();

        for (IRobotModule i : _modules)
            i.Start();
    }

    public void Update(){
        _battery.Update();

        for (IRobotModule i : _modules)
            i.Update();

        ToolTelemetry.Update();
    }

    public void Stop(){
        for (IRobotModule i : _modules)
            i.Stop();
    }

    protected void AddAdditionModules(ArrayList<Class<?>> modules){
        for (Class<?> i : modules)
            if (i.isInstance(IRobotModule.class)) {
                try {
                    _modules.add((IRobotModule) i.newInstance());
                } catch (Exception e){
                    throw new RuntimeException("not correct constructor in module " + i.getName());
                }
            }

        for (IRobotModule i : _modules)
            i.Init(this);
    }

    public  <T extends IRobotModule> T GetModule(Class<T> type){
        for(IRobotModule i : _modules)
            if(i.getClass() == type)
                return (T) i;

        throw new RuntimeException("not found " + type.getName() + "module");
    }

    public static class AnnotationFinder{
        private static final String ProjectPath = "org.woen.team18742";

        public static ArrayList<Class<?>> GetAnnotation(Class<?> annotation){
            ArrayList<Class<?>> result = new ArrayList<>();

            for (Class<?> i : findAllClassesUsingClassLoader(ProjectPath)){
                for(Annotation j: i.getAnnotations())
                    if(j.getClass().equals(annotation))
                        result.add(i);
            }

            return result;
        }

        private static Set<Class> findAllClassesUsingClassLoader(String packageName) {
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .collect(Collectors.toSet());
        }

        private static Class getClass(String className, String packageName) {
            try {
                return Class.forName(packageName + "."
                        + className.substring(0, className.lastIndexOf('.')));
            } catch (ClassNotFoundException e) {
                // handle the exception
            }
            return null;
        }
    }
}
