package org.woen.team17517.Programms.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;

@TeleOp

public class TeleOPNew extends LinearOpMode {
    Button liftUpBut = new Button();
    Button liftDownBut = new Button();
    private boolean liftUp = false;
    private boolean liftDown = false;

    private boolean brushIn = false;
    private boolean brushOut = false;

    UltRobot robot;
    public void runOpMode(){
        robot = new UltRobot(this);

        liftUp = gamepad1.dpad_up;
        liftDown = gamepad1.dpad_down;

        brushIn = gamepad1.left_trigger>0.1;
        brushOut = gamepad1.left_bumper;


        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;

        waitForStart();

        while(opModeIsActive()){
            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed = gamepad1.left_stick_x;
            angleSpeed = gamepad1.right_stick_x;


            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed*60000, forwardSpeed*60000, angleSpeed*60000);

            if (liftUpBut.update(liftUp)) {
               robot.lift.moveUP();
            }
            if (liftDownBut.update(liftDown)) {
                robot.lift.moveDown();
            }

            if (brushIn){
                robot.grabber.brushIn();
            }else{
                robot.grabber.brushOff();
            }
            if (brushOut){
                robot.grabber.brushOut();
            }else{
                robot.grabber.brushOff();
            }

            robot.allUpdate();
        }
    }
}
