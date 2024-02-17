package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.Lift.LiftMode;
import org.woen.team17517.RobotModules.Lift.LiftPosition;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;

@TeleOp
@Config
public class TeleOPNew extends LinearOpMode {
    Button liftUpBut = new Button();
    Button liftDownBut = new Button();
    Button openGrabberBut = new Button();
    Button closeGrabberBut = new Button();
    Button startPlaneBut = new Button();
    Button aimPlaneBut = new Button();
    Button openGrabberMunBut = new Button();
    Button closeGrabberMunBut = new Button();

    public static double aimPos = 0.35;
    public static double startPos = 0.9;
    public static double notStartPose = 0;
    public static double notAimedPos = 0;
    public static boolean telemetryTeleOp =false;
    boolean planeIsAimed = false;
    boolean planeIsStarted = false;
    String planeStatus = "Stay";
    UltRobot robot;
    TeleOpModules teleOpModules;
    public void runOpMode(){
        robot = new UltRobot(this);
        teleOpModules = new TeleOpModules(robot);
        robot.telemetryOutput.teleOp = telemetryTeleOp;

        waitForStart();

        while(opModeIsActive()){

            telemetry.addData("Lift",robot.lift.getLiftMode().toString()+robot.lift.getPosition().toString());
            telemetry.addData("Grabber",robot.grabber.getTargetProgib().toString()+robot.grabber.getTargetOpenClose());
            telemetry.addData("Plane",planeStatus);
            robot.telemetryOutput.teleOp = telemetryTeleOp;

            boolean liftUpAuto            = gamepad1.triangle;
            boolean liftDownAuto          = gamepad1.cross;

            boolean brushIn               = gamepad1.left_trigger > 0.1;
            boolean brushOut              = gamepad1.left_bumper;

            boolean openAndFinishGrabber  = gamepad1.circle;
            boolean closeAndSafeGrabber   = gamepad1.square;

            boolean liftUpMan             = gamepad1.dpad_up;
            boolean liftDownMan           = gamepad1.dpad_down;

            boolean openGrabberMun        = gamepad1.dpad_left;
            boolean closeGrabberMun       = gamepad1.dpad_right;

            boolean startPlane            = gamepad1.right_trigger>0.1;
            boolean aimPlane              = gamepad1.right_bumper;

            double forwardSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y);
            double sideSpeed    = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_x);
            double angleSpeed   = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x);

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);


            if      (liftUpBut.update(liftUpAuto))     teleOpModules.liftUpAndFinishGrabber();
            else if (liftDownBut.update(liftDownAuto)) teleOpModules.liftDownAndOpenGrabber();


            if (openGrabberBut.update(openAndFinishGrabber)&&robot.lift.getPosition()!=LiftPosition.DOWN) teleOpModules.openGrabber();
            else if (closeGrabberBut.update(closeAndSafeGrabber))                                         teleOpModules.closeGrabber();


            if(aimPlaneBut.update(aimPlane)&&!planeIsAimed){
                planeIsAimed = true;
                planeStatus = "aimed";
                robot.hardware.planeServos.aimPlaneServo.setPosition(aimPos);
            }else if(aimPlaneBut.update(aimPlane)&& planeIsAimed){
                planeIsAimed = false;
                planeStatus = "not aimed";
                robot.hardware.planeServos.aimPlaneServo.setPosition(notAimedPos);
            }

            if(startPlaneBut.update(startPlane)&&planeIsAimed&&!planeIsStarted){
                planeIsStarted = true;
                planeStatus = "start";
                robot.hardware.planeServos.startPlaneServo.setPosition(startPos);
            } else if (startPlaneBut.update(startPlane)&&planeIsStarted) {
                planeIsStarted = false;
                planeStatus = "not start";
                robot.hardware.planeServos.startPlaneServo.setPosition(notStartPose);
            }


            if (openGrabberMunBut.update(openGrabberMun))   robot.grabber.open();
            if (closeGrabberMunBut.update(closeGrabberMun)) robot.grabber.close();


            if (brushIn)       robot.grabber.brushIn();
            else if (brushOut) robot.grabber.brushOut();
            else               robot.grabber.brushOff();


            if(liftDownMan)                                          robot.lift.setManualTargetDown();
            else if (liftUpMan)                                      robot.lift.setManualTargetUp();
            else if (robot.lift.getLiftMode()==LiftMode.MANUALLIMIT) robot.lift.setStopManualTarget();

            robot.allUpdate();
        }
    }
}
