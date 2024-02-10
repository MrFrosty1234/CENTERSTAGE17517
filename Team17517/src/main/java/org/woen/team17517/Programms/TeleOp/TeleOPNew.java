package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.Transport.Lift.LiftMode;
import org.woen.team17517.RobotModules.Transport.Lift.LiftPosition;
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
    Button openGrabberMunBut = new Button();
    Button closeGrabberMunBut = new Button();
    public static double aimPos = 0.35;
    public static double startPos = 0.7;
    public static boolean telemetryTeleOp =false;
    UltRobot robot;
    public void runOpMode(){
        robot = new UltRobot(this);
        robot.telemetryOutput.teleOp = telemetryTeleOp;
        boolean planeAimed = false;

        waitForStart();

        while(opModeIsActive()){
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

            boolean startPlane            = gamepad1.ps;
            boolean aimPlane              = gamepad1.touchpad;


            double forwardSpeed;
            double sideSpeed;
            double angleSpeed;

            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed    = gamepad1.left_stick_x;
            angleSpeed   = gamepad1.right_stick_x;


            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed*60000, forwardSpeed*60000, angleSpeed*60000);

            if (liftUpBut.update(liftUpAuto)){
                telemetry.addData("liftMoving","UP");
                robot.updateWhilePositionFalse(new Runnable[]{
                        ()->robot.grabber.close(),
                        ()->robot.grabber.safe(),
                        ()->robot.lift.moveUP()
                });
                telemetry.addData("liftMoving","STAY");
            }else if (liftDownBut.update(liftDownAuto)) {
                telemetry.addData("liftMoving","DOWN");
                robot.updateWhilePositionFalse(new Runnable[]{
                        ()->robot.grabber.open(),
                        ()->robot.grabber.safe(),
                        ()->robot.lift.moveDown(),
                        ()->robot.grabber.down()
                });
                telemetry.addData("liftMoving","STAY");
            }

            if (openAndFinishGrabberBut.update(openAndFinishGrabber) && robot.lift.getEncoderPosition() > LiftPosition.DOWN.value){
                robot.updateWhilePositionFalse(new Runnable[]{
                        ()->robot.grabber.finish(),
                        ()->robot.grabber.open()
                });}
            else if (closeAndSAfeGrabberBut.update(closeAndSafeGrabber)) {
                robot.updateWhilePositionFalse(new Runnable[]{
                        ()-> robot.grabber.close(),
                        ()-> robot.grabber.safe()
                });
            }
            if (openGrabberMunBut.update(openGrabberMun))   robot.grabber.open();
            if (closeGrabberMunBut.update(closeGrabberMun)) robot.grabber.close();

            if (brushIn){
                robot.grabber.brushIn();
            }else if (brushOut) {
                robot.grabber.brushOut();
            }else{
                robot.grabber.brushOff();
            }

            if(liftDownMan){
                robot.lift.setManualTargetDown();
            }else if (liftUpMan) {
                robot.lift.setManualTargetUp();
            }else if (robot.lift.getLiftMode()==LiftMode.MANUALLIMIT){
                robot.lift.setStopManualTarget();
            }

            if(aimPlaneBut.update(aimPlane)){
                planeAimed = true;
                telemetry.addData("Plane","aimed");
                robot.devices.aimPlaneServo.setPosition(aimPos);
            }
            if(startPlaneBut.update(startPlane)&&planeAimed){
                telemetry.addData("Plane","Pli");
                robot.devices.startPlaneSrevo.setPosition(startPos);
            }

            robot.allUpdate();
        }
    }
}
