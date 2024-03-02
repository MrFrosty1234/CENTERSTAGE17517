package org.woen.team17517.Programms.TeleOp;
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends TeleOPBase{
    @Override
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
}
