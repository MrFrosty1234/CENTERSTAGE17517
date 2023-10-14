package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Collector {
    public Automatic Auto;
    public Manual Manual;
    private State _currentState;
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

        _currentState = State.MANUAL;

        _prevTriangle = false;
    }

    void Update() {
        if (CommandCode.gamepad1.triangle && !_prevTriangle) {
            if (_currentState == State.MANUAL)
                _currentState = State.AUTO;
            else
                _currentState = State.MANUAL;
        }

        _prevTriangle = CommandCode.gamepad1.triangle;

        if (_currentState == State.MANUAL)
            Manual.Update();
        else
            Auto.Update();

        Driver.Update();
        Lift.Update();
    }
}
