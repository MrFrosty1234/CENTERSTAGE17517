package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TeleOpCollector extends BaseCollector {
    public Manual Manual;
    private Odometry _odometry;

    public TeleOpCollector(LinearOpMode commandCode) {
        super(commandCode);

        Manual = new Manual(this);
        _odometry = new Odometry(this);
    }

    @Override
    public void Start(){
        Lift.Start();
    }

    @Override
    public void Update() {
        super.Update();
        Manual.Update();
        _odometry.Update();
        Lift.Update();
    }
}
