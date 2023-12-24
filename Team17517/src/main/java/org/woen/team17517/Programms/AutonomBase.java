package org.woen.team17517.Programms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.team17517.Programms.Autonomus.AutonomBaseClass;
import org.woen.team17517.Robot.UltRobot;

@Autonomous
public class AutonomBase extends AutonomBaseClass {
    UltRobot robot;
    @Override
    public Runnable[] getBlueRight(){
        robot = new UltRobot(this);
        return new Runnable[]{
                ()->robot.drivetrainNew.setTarget(60,0,0),
                ()->robot.timer.getTimeForTimer(0.1),
                ()->robot.drivetrainNew.setTarget(-60,0,0),
                ()->robot.timer.getTimeForTimer(0.1)
        };
    }

}
