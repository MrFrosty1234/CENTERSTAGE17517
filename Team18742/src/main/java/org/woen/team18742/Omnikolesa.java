package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Omnikolesa extends LinearOpMode {

    DcMotor leftForwardDrive = null;
    DcMotor rightForwardDrive = null;
    DcMotor grabberDrive = null;
    DcMotor leftBackDrive = null;
    DcMotor rightBackDrive = null;

    @Override
    public void runOpMode() throws InterruptedException {
        leftForwardDrive = hardwareMap.get(DcMotor.class, "leftmotor");
        rightForwardDrive = hardwareMap.get(DcMotor.class, "rightmotor");
        grabberDrive = hardwareMap.get(DcMotor.class, "grabbermotor");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftbackmotor");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightbackmotor");
        leftBackDrive.setDirection(REVERSE);
        leftForwardDrive.setDirection(REVERSE);
        //rightBackDrive.setDirection(REVERSE);
        //rightForwardDrive.setDirection(REVERSE);
        waitForStart();
        boolean buttonXold = false;
        boolean graber = false;
        while (opModeIsActive()) {
            double forward = gamepad1.left_stick_y;
            double side = gamepad1.left_stick_x;
            double rightStickY = gamepad1.left_stick_y;
            double rotate = gamepad1.right_stick_x;
            boolean buttonX = gamepad1.cross;
            leftForwardDrive.setPower(forward - side - rotate);
            rightBackDrive.setPower(forward - side + rotate);
            leftBackDrive.setPower(forward + side - rotate);
            rightForwardDrive.setPower(forward + side + rotate);
            if (buttonX && !buttonXold)
            {
                graber = !graber;
            }
            buttonXold = buttonX;
            if (graber)
            {
                grabberDrive.setPower(-1);
            } else
            {
                grabberDrive.setPower(0);
            }
        }
    }
}

