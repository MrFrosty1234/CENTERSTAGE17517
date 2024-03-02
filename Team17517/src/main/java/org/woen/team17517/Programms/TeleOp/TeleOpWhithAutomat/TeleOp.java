package org.woen.team17517.Programms.TeleOp.TeleOpWhithAutomat;

import static org.woen.team17517.Programms.TeleOp.TeleOpWhithAutomat.Situation.*;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    UltRobot robot;
    double  forwardSpeed   ;
    double  sideSpeed      ;
    double  angleSpeed     ;
    boolean eatPixels      ;
    boolean openGrabber    ;
    boolean liftUp         ;
    boolean liftDown       ;
    boolean liftCentre     ;
    double liftSpeed       ;
    boolean aim            ;
    boolean shoot          ;
    public void runOpMode(){
        robot = new UltRobot(this);
        waitForStart();
        robot.intake.on();
        while (opModeIsActive()){
            forwardSpeed          = -robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y* abs(gamepad1.left_stick_y));
            sideSpeed             = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_x* abs(gamepad1.left_stick_x));
            angleSpeed            = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x* abs(gamepad1.right_stick_x))*0.75;
            liftSpeed             = -robot.lift.getSpeedPercent(gamepad1.right_stick_y*abs(gamepad1.right_stick_y));

            eatPixels   = gamepad1.circle;
            openGrabber = gamepad1.right_bumper;
            liftUp      = gamepad1.left_bumper;
            liftCentre  = gamepad1.dpad_up;
            liftDown    = gamepad1.dpad_down;
            aim         = gamepad1.share;
            shoot       = gamepad1.ps;


            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);

            if (eatPixels)   robot.intake.setSituation(EATING);
            if (openGrabber) robot.intake.setSituation(SCORING);
            if (liftUp)      robot.intake.setSituation(WAITINGUP);
            if (liftCentre)  robot.intake.setSituation(WAITINGBACKDROPDOWN);
            if (liftDown)    robot.intake.setSituation(WAITINGDOWN);
            if (abs(liftSpeed)>50) robot.lift.setSpeed(liftSpeed);
            if(aim)          robot.plane.aim();
            if (shoot)       robot.plane.shoot();
            telemetry.addLine(robot.intake.getSituation().toString());
        }
    }
}
