package org.woen.team18742;

public class Collector {
    public Automatic Auto;
    public Manual Manual;
    public Lift Lift;
    public DriverTrain Driver;
    public CommandCode CommandCode;
    private boolean _prevTriangle;

    public Collector(CommandCode commandCode) {
        CommandCode = commandCode;

        Driver = new DriverTrain(this);
        Manual = new Manual(this);
        Auto = new Automatic(this);
        Lift = new Lift(this);

        _prevTriangle = false;
    }

    public void Start(){
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
