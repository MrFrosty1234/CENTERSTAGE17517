
package org.woen.team17517.Programms.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.Lift.LiftMode;
import org.woen.team17517.RobotModules.Lift.LiftPosition;

@TeleOp

public abstract class TeleOPForFedorov extends TeleOPBase {
 @Override
   public void main(){
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

     double forwardSpeed = -robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y);
     double sideSpeed    = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_x);
     double angleSpeed   = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x);
     robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);


     if      (liftUpBut.update(liftUpAuto))     teleOpModules.liftUpAndFinishGrabber();
     else if (liftDownBut.update(liftDownAuto)) teleOpModules.liftDownAndOpenGrabber();


     if (openGrabberBut.update(openAndFinishGrabber)&&robot.lift.getPosition()!= LiftPosition.DOWN.value) teleOpModules.openGrabber();
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


     if      (brushIn)       robot.grabber.brushIn();
     else if (brushOut) robot.grabber.brushOut();
     else               robot.grabber.brushOff();


     if      (liftDownMan)                                           robot.lift.setSpeed(-2000);
     else if (liftUpMan)                                       robot.lift.setSpeed(2000);
     else if (robot.lift.getLiftMode()== LiftMode.MANUALLIMIT) robot.lift.setSpeed(0);

     robot.allUpdate();
 }
}
