package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public Odometry Odometry;

    private Manual _m;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Odometry = new Odometry(this);
        Auto = new Automatic(this);
        _m = new Manual(this);
    }

    @Override
    public void Start() {
        //Auto.Start();
        Lift.Start();
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();
        _m.Update();

        if(CommandCode.gamepad1.square)
            Auto.Start();
    }
}
