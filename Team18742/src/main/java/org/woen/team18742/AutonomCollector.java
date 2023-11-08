package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public Odometry Odometry;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Auto = new Automatic(this);
        Odometry = new Odometry(this);
    }

    @Override
    public void Start() {
        Auto.Start();
        Lift.Start();
        Auto.SetSpeedWorldCoords(0, 0.6);
    }

    @Override
    public void Update() {
        Odometry.Update();
    }
}
