package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Modules.Brush;
import org.woen.team18742.Modules.Cachinger;
import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.OdometrsHandler;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

public class BaseCollector {
    public LinearOpMode Robot;
    public Gyroscope Gyro;
    public DriverTrain Driver;
    public org.woen.team18742.Modules.Lift.Lift Lift;
    public org.woen.team18742.Modules.Intake Intake;
    public ElapsedTime Time;
    public Brush Brush;
    private Cachinger _cachinger;
    public OdometrsHandler Odometrs;
    private Battery _battery;

    public BaseCollector(LinearOpMode robot){
        Robot = robot;

        Devices.Init(robot.hardwareMap);
        ToolTelemetry.SetTelemetry(Robot.telemetry);

        _battery = new Battery(this);
        _cachinger = new Cachinger();
        Time = new ElapsedTime();
        Lift = new Lift(this);
        Brush = new Brush(this);
        Odometrs = new OdometrsHandler(this);
        Gyro = new Gyroscope(this);
        Driver = new DriverTrain(this);
        Intake = new Intake(this);
    }

    public void Start(){
        Time.reset();
        Odometrs.Reset();
        Gyro.Reset();
        Lift.Start();
        Driver.ResetIncoder();
    }

    public void Update(){
        _battery.Update();
        _cachinger.Update();
        Lift.Update();
        Gyro.Update();
        Intake.Update();

        ToolTelemetry.Update();
    }

    public void Stop(){}
}
