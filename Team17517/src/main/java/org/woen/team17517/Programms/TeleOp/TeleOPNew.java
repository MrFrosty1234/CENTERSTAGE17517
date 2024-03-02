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
    public void buttonsUpdate(){
        liftUpAuto            = gamepad1.triangle;
        liftDownAuto          = gamepad1.cross;
        brushIn               = gamepad1.left_trigger > 0.1;
        brushOut              = gamepad1.left_bumper;
        openAndFinishGrabber  = gamepad1.circle;
        closeAndSafeGrabber   = gamepad1.square;
        liftUpMan             = gamepad1.dpad_up;
        liftDownMan           = gamepad1.dpad_down;
        openGrabberMun        = gamepad1.dpad_left;
        closeGrabberMun       = gamepad1.dpad_right;
        startPlane            = gamepad1.right_trigger>0.1;
        aimPlane              = gamepad1.right_bumper;
        forwardSpeed           = -robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y);
        sideSpeed              = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_x);
        angleSpeed             = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x);
    }
    public void runOpMode(){
        robot = new UltRobot(this);
        teleOpModules = new TeleOpModules(robot);
        robot.telemetryOutput.teleOp = telemetryTeleOp;

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Lift",robot.lift.getLiftMode().toString()+robot.lift.getPosition());
            telemetry.addData("Grabber",robot.grabber.getTargetProgib().toString()+robot.grabber.getTargetOpenClose());
            telemetry.addData("Plane",planeStatus);
            robot.telemetryOutput.teleOp = telemetryTeleOp;

            buttonsUpdate();

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);


            if      (liftUpBut.update(liftUpAuto))     teleOpModules.liftUpAndFinishGrabber();
            else if (liftDownBut.update(liftDownAuto)) teleOpModules.liftDownAndOpenGrabber();


            if (openGrabberBut.update(openAndFinishGrabber)&&robot.lift.getPosition() > LiftPosition.DOWN.value) teleOpModules.openGrabber();
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


            if(liftDownMan)                                          robot.lift.setSpeed(-2000);
            else if (liftUpMan)                                      robot.lift.setSpeed(2000);
            else if (robot.lift.getLiftMode()==LiftMode.MANUALLIMIT) robot.lift.setSpeed(0);

            robot.allUpdate();
        }
    }
    boolean liftUpAuto            ;
    boolean liftDownAuto          ;
    boolean brushIn               ;
    boolean brushOut              ;
    boolean openAndFinishGrabber  ;
    boolean closeAndSafeGrabber   ;
    boolean liftUpMan             ;
    boolean liftDownMan           ;
    boolean openGrabberMun        ;
    boolean closeGrabberMun       ;
    boolean startPlane            ;
    boolean aimPlane              ;
    double forwardSpeed           ;
    double sideSpeed              ;
    double angleSpeed             ;
}
