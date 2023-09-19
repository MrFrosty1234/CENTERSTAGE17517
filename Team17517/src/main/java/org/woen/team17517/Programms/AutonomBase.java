package org.woen.team17517.Programms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Robot.AiRRobot;


abstract public class AutonomBase extends LinearOpMode {
    AiRRobot aiRRobot;
    TelemetryPacket telemetryPacket;

    @Override
    public void runOpMode() {

        aiRRobot = new AiRRobot(this);
        telemetryPacket = new TelemetryPacket();
        telemetryPacket.fieldOverlay().strokeLine(0, 0, 141.3 / 2.54, 21.9 / 2.54);
        FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);
        waitForStart();
        main();
    }

    abstract public void main();
}
