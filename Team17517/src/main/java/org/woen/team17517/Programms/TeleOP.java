package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.UltRobot;

@TeleOp
public class TeleOP extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode(){

        double x;
        double y;
        double h;
        boolean right_bumper;
        boolean right_trigger;
        DcMotor left_front_drive;
        DcMotor left_back_drive;
        DcMotor right_front_drive;
        DcMotor motor;
        DcMotor right_back_drive;
        robot = new UltRobot(this);
        DcMotor grabber;
        waitForStart();
        while (opModeIsActive()){
            motor =robot.linearOpMode.hardwareMap.dcMotor.get("motor");
            grabber = robot.linearOpMode.hardwareMap.dcMotor.get("grabber");
            left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
            left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
            right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
            right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");
            x = gamepad1.left_stick_y;
            y = gamepad1.left_stick_x;
            double z = -gamepad1.right_stick_x;
            right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
            left_back_drive.setDirection(DcMotorSimple.Direction.FORWARD);
            double leftFrontMotorPower = x - y - z;
            double rightFrontMotorPower = x + y + z;
            double leftRearMotorPower = x + y - z;
            double rightRearMotorPower = x - y + z;
            left_front_drive.setPower(leftFrontMotorPower);
            left_back_drive.setPower(leftRearMotorPower);
            right_front_drive.setPower(rightFrontMotorPower);
            right_back_drive.setPower(rightRearMotorPower);


            right_bumper = gamepad1.right_bumper;
            right_trigger = gamepad1.right_trigger>0.1;
            robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;

            if(right_trigger){
                motor.setPower(0.7);
            } else if (right_bumper){
                motor.setPower(0.45);
            }else{
                motor.setPower(0.1);
            }
            ///////////////////////

            if (gamepad1.left_bumper){
                grabber.setPower(1);
            }else if (gamepad1.left_trigger>0.1){
                grabber.setPower(-1);
            }else{
                grabber.setPower(0);
            }
            robot.allUpdate();
        }
    }
}
