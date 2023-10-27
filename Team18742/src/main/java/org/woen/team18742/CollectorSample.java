package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class CollectorSample {
    public LinearOpMode CommandCode;
    public Gyroscope Gyro;
    public DriverTrain Driver;
    public Lift Lift;

    public CollectorSample(LinearOpMode commandCode){
        CommandCode = commandCode;

        Lift = new Lift(this);
        Gyro = new Gyroscope(this);
        Driver = new DriverTrain(this);
    }

    public void Start(){
    }

    public void Update(){
    }
}
