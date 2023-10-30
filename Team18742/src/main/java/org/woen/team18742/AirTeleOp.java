package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class AirTeleOp extends LinearOpMode {

    Collector collector = null;

    @Override
    public void runOpMode() throws InterruptedException {
        collector = new Collector(this);

    }
}
