package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.Transport.Lift.LiftMode;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;

@TeleOp
@Config
public class TeleOPNew extends LinearOpMode {
    Button liftUpBut = new Button();
    Button liftDownBut = new Button();
    Button openAndFinishGrabberBut = new Button();
    Button closeAndSAfeGrabberBut = new Button();
    Button startPlaneBut = new Button();
    Button aimPlaneBut = new Button();
    public static double aimPos = 0;
    public static double startPos = 0;
    UltRobot robot;
    public void runOpMode(){
        robot = new UltRobot(this);

        boolean liftUpAuto            = gamepad1.triangle;
        boolean liftDownAuto          = gamepad1.x;

        boolean brushIn               = gamepad1.left_trigger > 0.1;
        boolean brushOut              = gamepad1.left_bumper;

        boolean openAndFinishGrabber  = gamepad1.circle;
        boolean closeAndSafeGrabber   = gamepad1.square;

        boolean liftUpMan             = gamepad1.dpad_up;
        boolean liftDownMan           = gamepad1.dpad_down;

        boolean startPlane = gamepad1.ps;
        boolean aimPlane = gamepad1.right_stick_button;


        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;

        waitForStart();

        while(opModeIsActive()){
            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed = gamepad1.left_stick_x;
            angleSpeed = gamepad1.right_stick_x;


            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed*30000, forwardSpeed*30000, angleSpeed*30000);

            if (liftUpBut.update(liftUpAuto)) {
                robot.transportPixels.moveUp();
            }else if (liftDownBut.update(liftDownAuto)) {
                robot.transportPixels.moveDown();
            }

            if (openAndFinishGrabberBut.update(openAndFinishGrabber)){
                robot.transportPixels.finishGrabber();
            }else if (closeAndSAfeGrabberBut.update(closeAndSafeGrabber)) {
                robot.transportPixels.safeGrabber();
            }

            if (brushIn){
                robot.grabber.brushIn();
            }else if (brushOut) {
                robot.grabber.brushOut();
            }else{
                robot.grabber.brushOff();
            }

            if(liftDownMan){
                robot.transportPixels.lift.setManualTargetDown();
            }else if (liftUpMan) {
                robot.transportPixels.lift.setManualTargetUp();
            }else if (robot.transportPixels.lift.liftMode==LiftMode.MANUALLIMIT){
                robot.transportPixels.stayLift();
            }

            if(aimPlaneBut.update(aimPlane)){
                robot.devices.aimPlaneServo.setPosition(aimPos);
                if(startPlaneBut.update(startPlane)){
                    robot.devices.startPlaneSrevo.setPosition(startPos);
                }
            }

            robot.allUpdate();
        }
    }
}
