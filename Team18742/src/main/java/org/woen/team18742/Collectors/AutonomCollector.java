package org.woen.team18742.Collectors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Modules.Automatic;
import org.woen.team18742.Odometry.Odometry;

public class AutonomCollector extends BaseCollector {
    public Automatic Auto;
    public org.woen.team18742.Odometry.Odometry Odometry;

    public AutonomCollector(LinearOpMode commandCode) {
        super(commandCode);

        Odometry = new Odometry(this);
        Auto = new Automatic(this);
    }

    @Override
    public void Start() {
        super.Start();

        Odometry.Start();
    }

    @Override
    public void Update() {
        super.Update();
        Odometry.Update();

        Auto.Update();
    }
}
