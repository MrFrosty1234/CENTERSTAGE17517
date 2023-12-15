package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.Lighting;
import org.woen.team17517.Robot.UltRobot;

import static java.lang.Math.signum;

@TeleOp
public class TeleOpM extends LinearOpMode {
    public double speed = 1.0;
    boolean oldSquare = false;
    boolean graberPosition;
    DcMotor grabber;
    boolean oldCircle = false;
    boolean oldTriangle = false;
    boolean oldBumper = false;
    boolean oldDpad_Up = false;
    UltRobot robot;
    Servo pixelServo;
    Servo pixelServoLift;
    DcMotor right_back_drive;
    DcMotor left_back_drive;
    DcMotor left_front_drive;
    DcMotor right_front_drive;
    DcMotor lift;
    boolean speedcontrol = false;
    int k = 1;
    int kGrab = 1;
    public static double grabOpen = 0.25;
    public static double grabClose = 0;
    public static double perekidStart = 0.85;
    public static double perekidFinish = 0.45;

    @Override
    public void runOpMode() {
        robot = new UltRobot(this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        int grab = 0;
        waitForStart();
        robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;
        robot.lighting.lightMode = Lighting.LightningMode.SMOOTH;
        robot.lift.liftPosition = Lift.LiftPosition.ZERO;
        lift = robot.linearOpMode.hardwareMap.dcMotor.get("lift");
        pixelServo = robot.linearOpMode.hardwareMap.servo.get("pixelServo");
        pixelServoLift = robot.linearOpMode.hardwareMap.servo.get("pixelServoLift");
        grabber = robot.linearOpMode.hardwareMap.dcMotor.get("grabber");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        while (opModeIsActive()) {
            boolean dpad_up = gamepad1.dpad_up;
            boolean circle = gamepad1.circle;
            boolean square = gamepad1.square;
            boolean triangle = gamepad1.triangle;
            boolean cross = gamepad1.cross;
            boolean right_bumper = gamepad1.right_bumper;
            if (gamepad1.right_trigger > 0.1)
                k = -1;
            else
                k = 1;
            if (right_bumper) {
                grabber.setPower(k);
            } else {
                grabber.setPower(0);
            }
           /*if (square) {
                robot.grabber.pixelMotor.setPower(0.5 * k);
           }
           else
               robot.grabber.pixelMotor.setPower(0);

            if (gamepad1.ps) {
                robot.lift.reset();
            }*/

            /*if (gamepad1.dpad_down) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.ZERO;
                robot.lift.power =-0.5;
            }else{
                robot.lift.power =0;
            }

            if (gamepad1.dpad_up) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.UP;
                robot.lift.power =0.5;
            }else{
                robot.lift.power =0;
            }*/
/*
            if (gamepad1.dpad_left) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.LOW;
            }
            if (gamepad1.dpad_right) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.MIDDLE;
            }


 */
            right_back_drive.setPower(gamepad1.right_stick_x + gamepad1.right_stick_y - gamepad1.left_stick_x);
            right_front_drive.setPower(-gamepad1.right_stick_x + gamepad1.right_stick_y - gamepad1.left_stick_x);
            left_back_drive.setPower(-gamepad1.right_stick_x + gamepad1.right_stick_y + gamepad1.left_stick_x);
            left_front_drive.setPower(gamepad1.right_stick_x + gamepad1.right_stick_y + gamepad1.left_stick_x);
            /*if(dpad_up && !oldDpad_Up){
                robot.grabber.positionGrabber();
            }
            if(circle && !oldCircle){
                robot.grabber.positionPerekid();
            }*/
            //////////////////////////////////////
            //if (gamepad1.left_trigger>0.1) {
            //  robot.lift.liftMotor.setPower(1);

        }/* else if (triangle || cross) {
                robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;
            }*/
            /*if (triangle) {
                robot.lift.power = 1.0;
            }*/
        if (gamepad1.left_bumper) {
            lift.setPower(-1);
        }
        if (gamepad1.left_trigger > 0.1) {
            lift.setPower(1);
        } else {
            lift.setPower(0.1);
        }
            /*if (!gamepad1.right_bumper&& !(gamepad1.right_trigger>0.1)){
                robot.lift.setPowers(0.1);
            }*/
        if (gamepad1.cross) {
            pixelServoLift.setPosition(0);
            pixelServo.setPosition(0);
        } else {
            pixelServo.setPosition(1);
            pixelServoLift.setPosition(1);
        }

            /*if (!triangle && !cross) {
                robot.lift.power = 0.1;

            }*/


        robot.driveTrain.displayEncoders();

        double axial = -gamepad1.left_stick_y * speed;
        double lateral = -gamepad1.left_stick_x * speed;
        double yaw = -gamepad1.right_stick_x * speed;
        //robot.drivetrainNew.setTarget(gamepad1.left_stick_x,gamepad1.right_stick_y,gamepad1.right_stick_x);

        robot.allUpdate();

        if (gamepad1.right_bumper) {
            axial /= 1.5;
            lateral /= 1.5;
            yaw /= 1.5;
        }

        robot.grabber.enable(gamepad1.square);

        oldBumper = gamepad1.right_bumper;
        //robot.driveTrain.setPowers(moveLikeKTM(axial),moveLikeKTM(lateral),moveLikeKTM(yaw));
            /*oldSquare = square;
            oldCircle = circle;
            oldTriangle = triangle;
            oldDpad_Up = dpad_up;*/
        telemetry.update();

    }

    double moveLikeKTM(double power) {
        return power * power * signum(power);
    }
}
