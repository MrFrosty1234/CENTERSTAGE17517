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
    Button openAndFinishGrabberBut = new Button();
    Button closeGrabberBut = new Button();
    Button startPlaneBut = new Button();
    Button aimPlaneBut = new Button();
    Button openGrabberMunBut = new Button();
    Button closeGrabberMunBut = new Button();
    boolean planeAimed = false;
    public static double aimPos = 0.35;
    public static double startPos = 0.9;
    public static double closePose = 0;
    public static double fixPos = 0;
    public static boolean telemetryTeleOp =false;
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

            boolean startPlane            = gamepad2.dpad_up;
            boolean aimPlane              = gamepad2.dpad_right;
            boolean fixPlane           = gamepad2.dpad_down;
            boolean closePlane            = gamepad2.dpad_left;

            double forwardSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad2.left_stick_y);
            double sideSpeed    = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad2.left_stick_x);
            double angleSpeed   = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad2.right_stick_x);

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);

            if (liftUpBut.update(liftUpAuto))          teleOpModules.liftUpAndFinishGrabber();
            else if (liftDownBut.update(liftDownAuto)) teleOpModules.liftDownAndOpenGrabber();


            if (openAndFinishGrabberBut.update(openAndFinishGrabber) && robot.lift.getEncoderPosition() > LiftPosition.DOWN.value) {
                teleOpModules.openGrabber();
            }else if (closeGrabberBut.update(closeAndSafeGrabber)) {
                teleOpModules.closeGrabber();
            }
            if(aimPlaneBut.update(aimPlane)){
                planeStatus = "aimed";
                planeAimed = true;
                robot.hardware.planeServos.aimPlaneServo.setPosition(aimPos);
            }
            if(startPlaneBut.update(startPlane)&&planeAimed){
                planeStatus = "started";
                robot.hardware.planeServos.startPlaneServo.setPosition(startPos);
            }
            if (closePlane) robot.hardware.planeServos.startPlaneServo.setPosition(closePose);
            if (fixPlane) robot.hardware.planeServos.aimPlaneServo.setPosition(fixPos);

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
