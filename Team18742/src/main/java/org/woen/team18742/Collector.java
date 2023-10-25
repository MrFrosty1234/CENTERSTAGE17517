package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Collector {
    public Automatic Auto;
    public Manual Manual;
    public Lift Lift;
    public DriverTrain Driver;
    public LinearOpMode CommandCode;
    public Gyroscope Gyro;
    public Odometry Odometry;
    private boolean _prevTriangle;

    public Collector(LinearOpMode commandCode) {
        CommandCode = commandCode;

        Gyro = new Gyroscope(this);
        Driver = new DriverTrain(this);
        Manual = new Manual(this);
        Auto = new Automatic(this);
        Lift = new Lift(this);
        Odometry = new Odometry(this);

        _prevTriangle = false;
    }

    public void Start(){
        Lift.Start();

        if(CommandCode instanceof AutonomOpMode)
            Auto.Start();
    }

    public void Update() {
        if (CommandCode.gamepad1.triangle && !_prevTriangle) {
            Auto.Start();
        }

        _prevTriangle = CommandCode.gamepad1.triangle;

        Manual.Update();

        Driver.Update();
        Lift.Update();
        Odometry.Update();
    }
}
