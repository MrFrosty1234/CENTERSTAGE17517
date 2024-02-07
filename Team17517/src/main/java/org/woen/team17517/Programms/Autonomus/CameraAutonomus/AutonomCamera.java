package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCamera extends LinearOpMode {
    UltRobot robot;
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        ElementPosition elementPos = ElementPosition.FRONT;
        switch (elementPos){
            case FRONT:
                robot.autnomModules.Move(0,24000,0,1);
                robot.autnomModules.Move(0,-25000,0,1);
                break;
            case LEFT:
                robot.autnomModules.Move(0,24000,4000,0);
                robot.autnomModules.Move(0,-24000,-4000,0);
                break;
            case RIGHT:
                robot.autnomModules.Move(0,24000,-4000,0);
                robot.autnomModules.Move(0,-24000,4000,0);
                break;
        }
    }
}