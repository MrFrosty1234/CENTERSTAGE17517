package org.woen.team17517.Programms.TeleOp;

import static org.woen.team17517.RobotModules.Intake.State.*;

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
    boolean planeUp        ;
    boolean shoot          ;
    boolean planeDown      ;
    boolean revers;
    boolean backDropOff    ;
    public void runOpMode(){
        robot = new UltRobot(this);
        waitForStart();
        robot.plane.down();
        robot.intake.on();
        double tStart = System.currentTimeMillis() / 1000.0;
        while (opModeIsActive()){
            forwardSpeed          = -robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y*abs(gamepad1.left_stick_y));
            sideSpeed             = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.right_trigger-gamepad1.left_trigger+
                                                                                                gamepad1.left_stick_x*Math.abs(gamepad1.left_stick_x));
            angleSpeed            = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x* abs(gamepad1.right_stick_x))*0.75;
            double tNow = System.currentTimeMillis() / 1000.0;
            eatPixels   = gamepad1.right_bumper;
            openGrabber = gamepad1.left_bumper;
            liftUp      = gamepad1.dpad_up;
            liftCentre  = gamepad1.dpad_down;
            liftDown    = gamepad1.circle;
            planeUp     = gamepad1.triangle;
            planeDown   = gamepad1.cross;
            shoot       = gamepad1.square;
            revers = gamepad1.dpad_right;
            backDropOff = gamepad1.dpad_left;

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed,
                   robot.intake.getState()== WAITINGUP&&robot.intake.getState()
                   !=WAITINBACKDROPCENTER?forwardSpeed>0?forwardSpeed:forwardSpeed/2:forwardSpeed, angleSpeed);

            if (eatPixels)   robot.intake.setState( EATING);
            if (openGrabber) robot.intake.scoring();
            if (liftUp)      robot.intake.setState(WAITINGUP);
            if (liftCentre)  robot.intake.setState(WAITINBACKDROPCENTER);
            if (liftDown)    robot.intake.setState(WAITINGDOWN);
            if (planeUp)     robot.plane.up();
            if (planeDown)   robot.plane.down();
            if (shoot && (tNow - tStart) > 90)       robot.plane.shoot();
            if (revers)       robot.intake.setState(SAVEBRUSH);



            telemetry.addLine(robot.intake.getState().toString());
            telemetry.addData("Plane",robot.plane.getStatus().toString());
            robot.allUpdate();
        }
    }
}
