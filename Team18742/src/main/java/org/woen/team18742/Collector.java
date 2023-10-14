package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Collector extends LinearOpMode {
    private Automatic _automatic;
    private Manual _manual;
    private State _currentState;
    private DriverTrain _drivers;

    @Override
    public void runOpMode() {
        _automatic = new Automatic();
        _manual = new Manual();
        _drivers = new DriverTrain();

        _currentState = State.MANUAL;

        waitForStart();
        resetRuntime();

        boolean prevTriangle = false;

        while (opModeIsActive()) {
            if(gamepad1.triangle && !prevTriangle) {
                if(_currentState == State.MANUAL)
                    _currentState = State.AUTO;
                else
                    _currentState = State.MANUAL;
            }

            prevTriangle = gamepad1.triangle;

            if(_currentState == State.MANUAL)
                _manual.Update();
            else
                _automatic.Update();

            _drivers.Update();
        }
    }
}
