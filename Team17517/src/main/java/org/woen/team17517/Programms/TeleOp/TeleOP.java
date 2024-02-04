package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.RobotModules.Grabber.Grabber;
import org.woen.team17517.RobotModules.Lift.LiftPosition;
import org.woen.team17517.RobotModules.Lighting.Lighting;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.Vector2D;

@TeleOp
@Config
public class TeleOP extends LinearOpMode {
    UltRobot robot;
    DcMotor grabber;
    Grabber.PerekidPosition perekidPosition;
    public Servo pixelServoRight;
    public Servo pixelServoLeft;

    public static double grabberOpenLeft = 0.8;
    public static double grabberCloseLeft = 0.5;
    public static double grabberOpenRight = 0.1;
    public static double grabberCloseRight = 0.7;


    boolean triangle = false;
    Button x = new Button();
    Button a = new Button();
    private  enum TeleopMode{
        ROBOT,CENTRE
    }
    TeleopMode teleopMode = TeleopMode.ROBOT;
    @Override
    public void runOpMode() {
        robot = new UltRobot(this);

        grabber = robot.linearOpMode.hardwareMap.dcMotor.get("intakeMotor");
        pixelServoRight = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoRight");
        pixelServoLeft = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLeft");


        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;


        waitForStart();

        robot.lighting.lightMode = Lighting.LightningMode.SMOOTH;
        boolean liftAtTarget = true;

        while (opModeIsActive()) {

            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed = gamepad1.left_stick_x;
            angleSpeed = gamepad1.right_stick_x;

            forwardSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(forwardSpeed);
            sideSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(sideSpeed);
            angleSpeed = robot.driveTrainVelocityControl.angularVelocityPercent(angleSpeed);

            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);


            if (gamepad1.dpad_up) robot.lift.moveUP();
            else if (gamepad1.dpad_down) robot.lift.moveDown();


            if (gamepad1.left_bumper)
                grabber.setPower(1);
            else if (gamepad1.left_trigger > 0.1)
                grabber.setPower(-gamepad1.left_trigger);
            else
                grabber.setPower(0);


            if (x.update(gamepad1.right_trigger>0.1)) {
                pixelServoRight.setPosition(grabberOpenRight);
                pixelServoLeft.setPosition(grabberOpenLeft);
                telemetry.addData("Open",true);
            }else{
                telemetry.addData("Open",false);
            }
            if (a.update(gamepad1.right_bumper)){
                pixelServoRight.setPosition(grabberCloseRight);
                pixelServoLeft.setPosition(grabberCloseLeft);
                telemetry.addData("Close",true);
            }else{
                telemetry.addData("Close",false);
            }

            telemetry.addData("triger", gamepad1.right_trigger);
            telemetry.update();
            if (robot.lift.isAtPosition()) {

                if (robot.lift.getTargetPosition() == LiftPosition.UP)
                    perekidPosition = Grabber.PerekidPosition.FINISH;
                else perekidPosition = Grabber.PerekidPosition.START;

            } else perekidPosition = Grabber.PerekidPosition.LIFT_SAFE;

            robot.grabber.setPerekidPosition(perekidPosition);

            robot.allUpdate();
        }
    }
    public void openGraber() {
        pixelServoRight.setPosition(grabberOpenRight);
        pixelServoLeft.setPosition(grabberOpenLeft);
    }

    public void closeGraber() {
        pixelServoRight.setPosition(grabberCloseRight);
        pixelServoLeft.setPosition(grabberCloseLeft);
    }
}
