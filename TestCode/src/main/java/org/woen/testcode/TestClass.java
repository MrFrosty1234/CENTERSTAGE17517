package org.woen.testcode;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class TestClass  extends LinearOpMode {

    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    DcMotor grabberDrive = null;
    @Override
    public void runOpMode() throws InterruptedException {
        leftDrive  = hardwareMap.get(DcMotor.class, "leftmotor");
        rightDrive = hardwareMap.get(DcMotor.class, "rightmotor");
        grabberDrive = hardwareMap.get(DcMotor.class, "grabbermotor");
        leftDrive.setDirection(REVERSE);
        waitForStart();

        while(opModeIsActive()){
            double leftStickY = gamepad1.left_stick_y;
            double leftStickX = gamepad1.right_stick_x;
            boolean buttonX = gamepad1.cross;

            leftDrive.setPower(leftStickY+leftStickX);
            rightDrive.setPower(leftStickY+(-leftStickX));
            if(buttonX)
            {
                grabberDrive.setPower(-1);
            }
            else
            {
                grabberDrive.setPower(0);
            }
        }
    }
}
