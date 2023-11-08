package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class BaseCollector {
    public LinearOpMode CommandCode;
    public Gyroscope Gyro;
    public DriverTrain Driver;
    public Lift Lift;

    public BaseCollector(LinearOpMode commandCode){
        CommandCode = commandCode;

        Lift = new Lift(this);

        Gyro = new Gyroscope(this);
        Driver = new DriverTrain(this);
    }

    public void Start(){
    }

    public void Update(){
        Gyro.Update();
    }
}
