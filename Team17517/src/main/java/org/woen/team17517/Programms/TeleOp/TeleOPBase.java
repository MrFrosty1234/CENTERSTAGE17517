package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.woen.team17517.RobotModules.Intake.Lift.LiftMode;
import org.woen.team17517.RobotModules.Intake.Lift.LiftPosition;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.TelemetryOutput;

@Config
public abstract class TeleOPBase extends LinearOpMode {
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
    protected void buttonsUpdate(){}
    public void runOpMode(){
        robot = new UltRobot(this);
        teleOpModules = new TeleOpModules(robot);
        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Lift",robot.lift.getLiftMode().toString()+robot.lift.getPosition());
            telemetry.addData("Grabber",robot.grabber.getProgibTarget().toString()+robot.grabber.getOpenCloseTarget());
            telemetry.addData("Plane",planeStatus);

            TelemetryOutput.teleOp = telemetryTeleOp;

            buttonsUpdate();

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);


            if      (liftUpBut.update(liftUpAuto))     teleOpModules.liftUpAndFinishGrabber();
            else if (liftDownBut.update(liftDownAuto)) teleOpModules.liftDownAndOpenGrabber();


            if (openGrabberBut.update(openAndFinishGrabber)&&robot.lift.getPosition() > LiftPosition.DOWN.get()) teleOpModules.openGrabber();
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


            if (openGrabberMunBut.update(openGrabberMun))   robot.grabber.open();robot.grabber.backWallOpen();
            if (closeGrabberMunBut.update(closeGrabberMun)) robot.grabber.close();robot.grabber.backWallClose();


            if (brushIn)       robot.brush.in();
            else if (brushOut) robot.brush.out();
            else               robot.brush.off();


            if(liftDownMan)                                          ;
            else if (liftUpMan)                                      ;
            else if (robot.lift.getLiftMode()==LiftMode.MANUALLIMIT) ;

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
