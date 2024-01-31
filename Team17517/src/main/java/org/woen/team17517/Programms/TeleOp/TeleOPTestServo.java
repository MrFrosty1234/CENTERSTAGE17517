package org.woen.team17517.Programms.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.RobotModules.Grabber.Grabber;
import org.woen.team17517.RobotModules.Lift.LiftPosition;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.Vector2D;

@TeleOp
public class TeleOPTestServo extends LinearOpMode {
    UltRobot robot;
    DcMotor grabber;
    Grabber.PerekidPosition perekidPosition;

    boolean triangle = false;
    Button triangleButton = new Button();

    private enum TeleopMode {
        ROBOT, CENTRE
    }

    TeleopMode teleopMode = TeleopMode.CENTRE;

    @Override
    public void runOpMode() {
        robot = new UltRobot(this);

        grabber = robot.linearOpMode.hardwareMap.dcMotor.get("intakeMotor");

        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;

        waitForStart();


        boolean liftAtTaget = true;

        while (opModeIsActive()) {
            triangle = gamepad1.triangle;

            if (gamepad1.dpad_up) robot.lift.moveUP();
            else if (gamepad1.dpad_down) robot.lift.moveDown();


            if (gamepad1.left_bumper)
                grabber.setPower(1);
            else if (gamepad1.left_trigger > 0.1)
                grabber.setPower(-gamepad1.left_trigger);
            else
                grabber.setPower(0);


            if (gamepad1.circle) {
                robot.grabber.closeRightGraber();
                robot.grabber.closeLeftGraber();
            }
            if (gamepad1.cross) {
                robot.grabber.openRightGraber();
                robot.grabber.openLeftGraber();
            }
        }
    }
}
