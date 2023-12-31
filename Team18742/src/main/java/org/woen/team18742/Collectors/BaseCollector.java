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

import java.util.ArrayList;

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

        AddAditionModules(Module.class.getClasses());
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

    protected void AddAditionModules(Class<?>[] modules){
        try {
            for (Class<?> i : modules)
                if (i.isInstance(IRobotModule.class))
                    _modules.add((IRobotModule) i.newInstance());
        }
        catch (Exception ignored){}

        for (IRobotModule i : _modules)
            i.Init(this);
    }

    public IRobotModule GetModule(Class<? extends IRobotModule> type){
        for(IRobotModule i : _modules)
            if(i.getClass() == type)
                return i;

        throw new RuntimeException("not found " + type.getName() + "module");
    }
}
