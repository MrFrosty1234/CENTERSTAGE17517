package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Collector extends CollectorSample {
    public Manual Manual;
    public Lift Lift;
    public LinearOpMode CommandCode;
    private boolean _prevTriangle;

    public Collector(LinearOpMode commandCode) {
        super(commandCode);

        Manual = new Manual(this);
        Lift = new Lift(this);

        _prevTriangle = false;
    }

    @Override
    public void Start(){
        Lift.Start();
    }

    @Override
    public void Update() {
        Manual.Update();

        Lift.Update();
    }
}
