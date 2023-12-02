package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TeleOpCollector extends BaseCollector {
    public Manual Manual;

    public TeleOpCollector(LinearOpMode commandCode) {
        super(commandCode);

        Manual = new Manual(this);
    }

    @Override
    public void Start(){
        Lift.Start();
        Manual.Start();
    }

    @Override
    public void Update() {
        super.Update();
        Manual.Update();
        Lift.Update();
    }
}