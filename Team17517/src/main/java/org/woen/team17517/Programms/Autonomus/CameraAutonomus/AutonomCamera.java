package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;

@Autonomous
public class AutonomCamera extends LinearOpMode {
    UltRobot robot;
    boolean dpadUp = false;
    boolean dpadDown = false;
    boolean dpadRight = false;
    Button dpadRightButton = new Button();
    boolean dpadLeft = false;
    Button dpadLeftButton = new Button();
    Button dpadDownButton = new Button();
    Button dpadUpButton = new Button();
    boolean rightBumper = false;
    Button rightBumperButton   = new Button();
    boolean rightTriger = false;
    Button rigtTrigerButton = new Button();
    StartTeam startTeam = StartTeam.BlUE;
    StartPosition startPosition = StartPosition.NEAR_BACKBOARD;
    @Override
    public void waitForStart() {
        long  timeToSleep= 0;
        while (!isStarted()) {
            dpadDown = gamepad1.dpad_down;
            dpadUp = gamepad1.dpad_up;
            dpadLeft = gamepad1.dpad_left;
            dpadRight = gamepad1.dpad_right;
            rightBumper = gamepad1.right_bumper;
            rightTriger = gamepad1.right_trigger > 0;

            if (dpadDownButton.update(dpadDown)){
                startTeam = StartTeam.BlUE;
            }
            if (dpadUpButton.update(dpadUp)){
                startTeam = StartTeam.RED;
            }
            if (dpadLeftButton.update(dpadLeft)){
                startPosition = StartPosition.FAR_BACKBOARD;
            }
            if (dpadRightButton.update(dpadRight)){
                startPosition = StartPosition.NEAR_BACKBOARD;
            }
            if (rightBumperButton.update(rightBumper)){
                timeToSleep += 1000;
            }
            if (rigtTrigerButton.update(rightTriger)){
                timeToSleep += 5000;
            }

            telemetry.addData("Team",startTeam);
            telemetry.addData("Position",startPosition);
            telemetry.addData("Time to sleep", timeToSleep);
            telemetry.update();
        }
        sleep(timeToSleep);
    }


    enum StartTeam{
        BlUE,
        RED
    }
    enum StartPosition{
        NEAR_BACKBOARD,
        FAR_BACKBOARD
    }

    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        ElementPosition elementPos = ElementPosition.FRONT;///HERE
        switch (elementPos){
            case FRONT:
                robot.autnomModules.Move(0,24000,0,1);
                robot.autnomModules.Move(0,-25000,0,1);
                break;
            case LEFT:
                robot.autnomModules.Move(0,24000,4000,1);
                robot.autnomModules.Move(0,-24000,-4000,1);
                break;
            case RIGHT:
                robot.autnomModules.Move(0,24000,-4000,1);
                robot.autnomModules.Move(0,-24000,4000,1);
                break;
        }
        switch (startTeam){
            case RED:
                switch (startPosition){
                    case FAR_BACKBOARD:
                    break;
                    case NEAR_BACKBOARD:
                    break;
                }
            break;
            case BlUE:
                switch (startPosition){
                    case FAR_BACKBOARD:
                        robot.autnomModules.Move(24000,0,0,1);
                        robot.autnomModules.Move(0,24000,0,1);
                        robot.autnomModules.eatPixels();
                        robot.autnomModules.Move(0,24000,0,1);
                        robot.autnomModules.Move(0,0,6000,1);
                        robot.autnomModules.Move(0,12000,0,1);
                        robot.autnomModules.Move(0,12000,0,1);
                        robot.autnomModules.Move(0,-25000,0,5);
                        robot.autnomModules.Move(24000,0,0,1);
                        robot.autnomModules.Move(0,-12000,0,3);
                        robot.autnomModules.bacBoardPixels();
                    break;
                    case NEAR_BACKBOARD:
                        robot.autnomModules.Move(24000,0,0,1);
                        robot.autnomModules.Move(0,24000,0,1);
                        robot.autnomModules.Move(0,0,6000,1);
                        robot.autnomModules.Move(0,-24000,0,2);
                        robot.autnomModules.bacBoardPixels();
                        robot.autnomModules.Move(-24000,24000,0,1);
                        robot.autnomModules.Move(0,30000,0,5);
                        robot.autnomModules.eatPixels();
                        robot.autnomModules.Move(0,-30000,0,5);
                        robot.autnomModules.Move(24000,-24000,0,1);
                        robot.autnomModules.Move(0,-24000,0,0.5);
                        robot.autnomModules.bacBoardPixels();
                        break;
                }
            break;
        }

    }
}