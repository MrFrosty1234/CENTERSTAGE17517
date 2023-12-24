package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.Robot.Grabber;
import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.UltRobot;

@TeleOp
public class  TeleOP extends LinearOpMode {
    UltRobot robot;

    @Override
    public void runOpMode() {

        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;
        boolean right_bumper;
        double right_trigger;
        DcMotor left_front_drive;
        DcMotor left_back_drive;
        DcMotor right_front_drive;
        DcMotor liftMotor;
        DcMotor right_back_drive;
        robot = new UltRobot(this);
        DcMotor grabber;
        waitForStart();

        Grabber.PerekidPosition perekidPosition;

        boolean liftAtTaget = true;

        while (opModeIsActive()) {
            liftMotor = robot.linearOpMode.hardwareMap.dcMotor.get("liftMotor");
            grabber = robot.linearOpMode.hardwareMap.dcMotor.get("intakeMotor");
            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed = -gamepad1.left_stick_x;
            angleSpeed = gamepad1.right_stick_x;
            forwardSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(forwardSpeed);
            sideSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(sideSpeed);
            angleSpeed = robot.driveTrainVelocityControl.angularVelocityPercent(angleSpeed);

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);



            if (gamepad1.dpad_up) robot.lift.targetPosition = Lift.LiftPosition.UP;
            else if (gamepad1.dpad_down) robot.lift.targetPosition = Lift.LiftPosition.DOWN;


            ///////////////////////

            if (gamepad1.left_bumper)
                grabber.setPower(1);
            else if (gamepad1.left_trigger > 0.1)
                grabber.setPower(-gamepad1.left_trigger);
            else
                grabber.setPower(0);


            if (gamepad1.a || (robot.lift.targetPosition == Lift.LiftPosition.UP && !liftAtTaget))
                robot.grabber.closeGraber();
            if (gamepad1.b) robot.grabber.openGraber();


            if (robot.lift.isAtPosition()) {
                if (robot.lift.targetPosition == Lift.LiftPosition.UP)
                    perekidPosition = Grabber.PerekidPosition.FINISH;
                else
                    perekidPosition = Grabber.PerekidPosition.START;
            } else
                perekidPosition = Grabber.PerekidPosition.LIFT_SAFE;

            robot.grabber.setPerekidPosition(perekidPosition);


            robot.allUpdate();
        }
    }
}
