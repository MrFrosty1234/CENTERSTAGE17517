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
        Driver.DriveDirection(0.5, 0, 0);
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();
    }
}
