package org.woen.team17517.Programms.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.RobotModules.Grabber.Grabber;
import org.woen.team17517.RobotModules.Grabber.GrabberMode;
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
    public Servo pixelServoLift;

    public static double grabberOpenLeft = 0.7;
    public static double grabberCloseLeft = 0.1;
    public static double grabberOpenRight = 0.1;
    public static double grabberCloseRight = 0.7;

     public static double perekidStartDown = 0.85;
     public static double perekidStart = 0.85;
     public static double perekidFinish = 0.3;boolean triangle = false;
    Button x = new Button();
    Button a = new Button();
    Button square = new Button();
    Button y = new Button();
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
        pixelServoLift = this.robot.linearOpMode.hardwareMap.get(Servo.class,"pixelServoLift");

        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;


        waitForStart();

        robot.lighting.lightMode = Lighting.LightningMode.SMOOTH;
        boolean liftAtTarget = true;
        boolean posGrabber = true;


        while (opModeIsActive()) {

            forwardSpeed = -gamepad1.left_stick_y;
            sideSpeed = gamepad1.left_stick_x;
            angleSpeed = gamepad1.right_stick_x;


            robot.driveTrainVelocityControl.moveRobotCord(sideSpeed*60000, forwardSpeed*60000, angleSpeed*60000);


            if (gamepad1.dpad_up){ robot.lift.moveUP();
            pixelServoLift.setPosition(perekidStart);}
            else if (gamepad1.dpad_down) robot.lift.moveDown();{
            }


            if (gamepad1.left_bumper)
                grabber.setPower(-1);
            else if (gamepad1.left_trigger > 0.1)
                grabber.setPower(gamepad1.left_trigger);
            else
                grabber.setPower(0);

            if (a.update(gamepad1.right_bumper)){
                pixelServoRight.setPosition(grabberOpenRight);
                pixelServoLeft.setPosition(grabberOpenLeft);
                telemetry.addData("Open",true);
                posGrabber = true;
            }else{
                telemetry.addData("Open",false);
            }
            if (x.update(gamepad1.right_trigger>0.1)){
                pixelServoRight.setPosition(grabberCloseRight);
                pixelServoLeft.setPosition(grabberCloseLeft);
                telemetry.addData("Close",true);
                posGrabber = false;
            }else{
                telemetry.addData("Close",false);
            }

            telemetry.addData("triger", gamepad1.right_trigger);

            if (square.update(gamepad1.b)) {
                pixelServoLift.setPosition(perekidFinish);
                pixelServoRight.setPosition(grabberOpenRight);
                pixelServoLeft.setPosition(grabberOpenLeft);
            }
            else if (y.update(gamepad1.y)) {
                pixelServoLift.setPosition(perekidStart);
                pixelServoRight.setPosition(grabberCloseRight);
                pixelServoLeft.setPosition(grabberCloseLeft);
            }

            telemetry.addData("square", gamepad1.b);
            telemetry.update();
            robot.allUpdate();
        }
    };
    
}
                                