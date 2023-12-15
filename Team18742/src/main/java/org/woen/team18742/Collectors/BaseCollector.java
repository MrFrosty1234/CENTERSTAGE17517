package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Tools.ToolTelemetry;

public class BaseCollector {
    public LinearOpMode CommandCode;
    public Gyroscope Gyro;
    public DriverTrain Driver;
    public org.woen.team18742.Modules.Lift.Lift Lift;
    public org.woen.team18742.Modules.Intake Intake;
    public ElapsedTime Time;

    public BaseCollector(LinearOpMode commandCode){
        CommandCode = commandCode;

        ToolTelemetry.SetTelemetry(CommandCode.telemetry);

        Lift = new Lift(this);
        Time = new ElapsedTime();
        Gyro = new Gyroscope(this);
        Driver = new DriverTrain(this);
        Intake = new Intake(this);
    }

    public void Start(){
        Time.reset();
        Lift.Start();
    }

    public void Update(){
        Lift.Update();
        Gyro.Update();
        Intake.Update();
    }

    public void Stop(){

    }
}
