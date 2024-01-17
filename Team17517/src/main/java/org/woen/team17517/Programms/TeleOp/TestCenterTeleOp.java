package org.woen.team17517.Programms.TeleOp;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.linearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

public class TestCenterTeleOp extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode(){
             robot = new UltRobot(this);
             
    }
}
