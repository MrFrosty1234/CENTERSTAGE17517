package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Collector {
    public Automatic Auto;
    public Manual Manual;
    public Lift Lift;
    public DriverTrain Driver;
    public LinearOpMode CommandCode;
    private boolean _prevTriangle;

    public Collector(LinearOpMode commandCode) {
        CommandCode = commandCode;

        Driver = new DriverTrain(this);
        Manual = new Manual(this);
        Auto = new Automatic(this);
        Lift = new Lift(this);

        _prevTriangle = false;
    }

    public void Start(){
        Lift.Start();
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
    }
}
