package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AutonomCollector extends CollectorSample{
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
    }

    @Override
    public void Update() {
        Odometry.Update();
    }
}
