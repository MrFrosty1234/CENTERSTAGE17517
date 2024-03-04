package org.woen.team17517.Programms.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.UltRobot;
@TeleOp
public class GrabberTest extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            boolean perekidFinish = gamepad1.circle;
            boolean perekidDown   = gamepad1.square;
            boolean backWallOpen  = gamepad1.cross;
            boolean backWallClose = gamepad1.triangle;
            boolean open  = gamepad1.dpad_up;
            boolean close = gamepad1.dpad_down;
            if(perekidFinish) robot.grabber.finish();
            if(perekidDown) robot.grabber.down();
            if(backWallOpen) robot.grabber.backWallOpen();
            if(backWallClose) robot.grabber.backWallClose();
            if(open) robot.grabber.open();
            if (close) robot.grabber.close();
            telemetry.addLine(robot.grabber.getOpenCloseTarget().toString());
            telemetry.addLine(robot.grabber.getProgibTarget().toString());
            telemetry.addData("BackOpen",backWallOpen);
            telemetry.addData("Wall",robot.grabber.getBackWallTarget().toString());
            robot.allUpdate();
        }
    }
}
