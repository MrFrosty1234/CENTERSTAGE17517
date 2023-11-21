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
    }

    @Override
    public void Start() {
        Lift.Start();

        Auto.PIDMove(10, 0);
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();

        Auto.Update();
    }
}
