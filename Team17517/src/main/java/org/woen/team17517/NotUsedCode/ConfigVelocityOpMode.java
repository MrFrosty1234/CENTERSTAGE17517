package org.woen.team17517.NotUsedCode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team17517.RobotModules.UltRobot;
import static java.lang.Math.signum;

public class ConfigVelocityOpMode extends LinearOpMode {
    UltRobot robot;
    public static double x=0;
    public static double y=0;
    public static double rf=0;
    public static double rb =0;
    public static double lf=0;
    public static double lb =0;
    public static double h=0;
    public  DcMotorEx left_front_drive;
    public  DcMotorEx left_back_drive;
    public  DcMotorEx right_front_drive;
    public  DcMotorEx right_back_drive;
    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        left_front_drive = hardwareMap.get(DcMotorEx.class, "left_front_drive");
        left_back_drive =  hardwareMap.get(DcMotorEx.class, "left_back_drive");

        right_front_drive = hardwareMap.get(DcMotorEx.class, "right_front_drive");
        right_back_drive =  hardwareMap.get(DcMotorEx.class, "right_back_drive");/*
        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
         */
        waitForStart();
        while(opModeIsActive()){
            double leftStickX = gamepad1.left_stick_x;
            double leftStickY = -gamepad1.left_stick_y;
            double rightStickX = gamepad1.right_stick_x;

            //left_front_drive.setPower(lf);
            //left_back_drive.setPower(lb);
            //right_front_drive.setPower(rf);
            //right_back_drive.setPower(rb);
/*
            telemetry.addData("lf",left_front_drive.getCurrentPosition());
            telemetry.addData("lb",left_back_drive.getCurrentPosition());
            telemetry.addData("rf",right_front_drive.getCurrentPosition());
            telemetry.addData("rb",right_back_drive.getCurrentPosition());

            telemetry.update();
            */
            robot.driveTrainVelocityControl.moveRobotCord(x,y,h);
            robot.allUpdate();

        }
    }
}
