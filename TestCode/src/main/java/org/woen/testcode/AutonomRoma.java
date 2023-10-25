package org.woen.testcode;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class AutonomRoma extends LinearOpMode {

    double targetDegrees;
    DcMotor leftForwardDrive = null;
    DcMotor rightForwardDrive = null;
    DcMotor leftBackDrive = null;
    DcMotor rightBackDrive = null;
    IMU imu;
    RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
    RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
    private DistanceSensor sensorDistance;

    void resetEncoder() {
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    void moveForward(double distance) {
        resetEncoder();

        double errold = 0;
        double kp = 1;
        double kd = 1;
        double time = 0;
        double timeold = 0;
        double rastoyanie = sensorDistance.getDistance(DistanceUnit.CM);
        double err = distance - rastoyanie;
        while (opModeIsActive() && abs(err) > 2) {
            rastoyanie = sensorDistance.getDistance(DistanceUnit.CM);
            err = distance - rastoyanie;
            time = System.currentTimeMillis() / 1000.0;
            double u = (err * kp) + (err - errold) * kd / (time - timeold);
            errold = err;
            timeold = time;
            leftForwardDrive.setPower(u);
            rightBackDrive.setPower(u);
            leftBackDrive.setPower(u);
            rightForwardDrive.setPower(u);

        }
        leftForwardDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightForwardDrive.setPower(0);
    }

    void turnGyro(double degrees) {
        imu.resetYaw();
        double errold = 0;
        double kp = 1;
        double kd = 1;
        double time = 0;
        double timeold = 0;
        targetDegrees = degrees;
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double err = degrees - orientation.getYaw(AngleUnit.DEGREES);
        while (opModeIsActive() && abs(err) > 2) {
            orientation = imu.getRobotYawPitchRollAngles();
            err = degrees - orientation.getYaw(AngleUnit.DEGREES);
            time = System.currentTimeMillis() / 1000.0;
            double u = (err * kp) + (err - errold) * kd / (time - timeold);
            errold = err;
            timeold = time;
            if (err > 180) {
                err = degrees - 360;
            }
            if (err > (-180)) {
                err = degrees + 360;
            }
            leftForwardDrive.setPower(u);
            rightBackDrive.setPower(-u);
            leftBackDrive.setPower(u);
            rightForwardDrive.setPower(-u);

        }
        leftForwardDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightForwardDrive.setPower(0);
    }


    void distanceSensor(double distance) {
        imu.resetYaw();
        double errold = 0;
        double kp = 1;
        double kd = 1;
        double kp1 = 1;
        double kd1 = 1;
        double time = 0;
        double timeold = 0;
        double time1 = 0;
        double timeold1 = 0;
        double rastoyanie = 0;
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double errTurn = targetDegrees - orientation.getYaw(AngleUnit.DEGREES);
        double errDistance = distance - rastoyanie;
        double uForward = (errDistance * kp1) + (errDistance - errold) * kd1 / (time1 - timeold1);

        while (opModeIsActive() && abs(errTurn) > 2 && abs(errDistance) > 2) {
            orientation = imu.getRobotYawPitchRollAngles();
            errTurn = targetDegrees - orientation.getYaw(AngleUnit.DEGREES);
            if (errTurn > 180) {
                errTurn = errTurn - 360;
            }
            if (errTurn < (-180)) {
                errTurn = errTurn + 360;
            }
            time = System.currentTimeMillis() / 1000.0;
            time1 = System.currentTimeMillis() / 1000.0;
            double uTurn = (errTurn * kp) + (errTurn - errold) * kd / (time - timeold);
            errold = errTurn;
            timeold1 = time1;
            timeold = time;
            rastoyanie = sensorDistance.getDistance(DistanceUnit.CM);



            leftForwardDrive.setPower(uTurn+uForward);
            rightBackDrive.setPower(-uTurn+uForward);
            leftBackDrive.setPower(uTurn+uForward);
            rightForwardDrive.setPower(-uTurn+uForward);

        }
    }
    void encoder(double distance) {
        resetEncoder();
        double diametr = 9.8;
        double encoderconstat = 1440;
        double errold;
        double kp = 1;
        double kd = 1;
        double rastoyanie = PI*diametr;
        double err = distance - rastoyanie;
        errold = err;
        while (opModeIsActive() && abs(err) > 2) {
            rastoyanie = (leftBackDrive.getCurrentPosition()+rightForwardDrive.getCurrentPosition()+leftForwardDrive.getCurrentPosition()+rightBackDrive.getCurrentPosition())/4.0/encoderconstat*PI*diametr;
            err = distance - rastoyanie;
            double u = (err * kp) + (err - errold) * kd;
            leftForwardDrive.setPower(u);
            rightBackDrive.setPower(u);
            leftBackDrive.setPower(u);
            rightForwardDrive.setPower(u);

        }
        leftForwardDrive.setPower(0);
        rightBackDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightForwardDrive.setPower(0);
    }

    @Override
        public void runOpMode () throws InterruptedException {
            leftForwardDrive = hardwareMap.get(DcMotor.class, "leftmotor");
            rightForwardDrive = hardwareMap.get(DcMotor.class, "rightmotor");
            leftBackDrive = hardwareMap.get(DcMotor.class, "leftmotor");
            rightBackDrive = hardwareMap.get(DcMotor.class, "rightmotor");
            leftForwardDrive.setDirection(REVERSE);
            leftBackDrive.setDirection(REVERSE);
            imu = hardwareMap.get(IMU.class, "imu");
            imu.initialize(new IMU.Parameters(orientationOnRobot));
            sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_distance");
            telemetry.addData(">>", "Press start to continue");
            telemetry.update();
            waitForStart();
            encoder(15);
            turnGyro(90);
            distanceSensor(20);

        }
    }

