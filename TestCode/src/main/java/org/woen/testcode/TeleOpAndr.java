package org.woen.testcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TeleOpAndr extends LinearOpMode {

    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    DcMotor chto =null;
    void dviz(double forward,double rotate){
        leftDrive.setPower(rotate + forward);
        rightDrive.setPower(-rotate + forward);

    }
    @Override
    public void runOpMode() throws InterruptedException {
        // Инициализация
        leftDrive = hardwareMap.get(DcMotor.class, "leftmotor");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE); // Поставить направление мотора

        rightDrive = hardwareMap.get(DcMotor.class, "rightmotor");
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        double enc = rightDrive.getCurrentPosition();

        chto = hardwareMap.get(DcMotor.class, "xerznaetchto");
        chto.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart(); // Запуск

        leftDrive.setPower(0.5); // Поехать на 50%

        sleep(1000); // Задержка на 1000 мс

        leftDrive.setPower(0);


        // Один раз - как void setup()
        int a=0;
        boolean gamepadsquarold = false;
        while (opModeIsActive()) {

            double gamepadLeftStickX = gamepad1.left_stick_x;
            double forward = gamepad1.left_stick_y;
            double gamepadRightStickY = gamepad1.right_stick_y;
            double rotate = gamepad1.right_stick_x;

            leftDrive.setPower(rotate + forward);
            rightDrive.setPower(-rotate + forward);

            boolean gamepadsquar = gamepad1.square;


            if (gamepadsquar != gamepadsquarold && gamepadsquar){
                a=a+1;
            }
            if (a==2){
                a=0;
            }
            //oleksander
            chto.setPower(a);
            gamepadsquarold = gamepadsquar;
        }
    }
}
