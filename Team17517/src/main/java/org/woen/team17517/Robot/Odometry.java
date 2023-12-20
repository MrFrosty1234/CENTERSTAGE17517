package org.woen.team17517.Robot;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.woen.team17517.R;

public class Odometry implements RobotModule {
    public double x = 0, y = 0, heading = 0;
    double gyro;
    double encoderToCm = 24 * 20 / (9.8 * PI) / (124 / 121.8);

    double lfdold = 0;
    double lbdold = 0;
    double rfdold = 0;
    double rbdold = 0;
    UltRobot robot;

    private final DcMotor left_front_drive;
    private final DcMotor left_back_drive;
    private final DcMotor right_front_drive;
    private final DcMotor right_back_drive;

    public Odometry(UltRobot robot) {
        this.robot = robot;
        gyro = robot.gyro.getAngle();

        left_front_drive = this.robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = this.robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = this.robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = this.robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");

        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setDirection(DcMotor.Direction.FORWARD);
        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update() {
        int lfd = left_front_drive.getCurrentPosition();
        int lbd = left_back_drive.getCurrentPosition();
        int rfd = right_front_drive.getCurrentPosition();
        int rbd = right_back_drive.getCurrentPosition();
        double angle = robot.gyro.getAngle();

        double deltaX = wheelsToXPosition(lfd - lfdold, lbd - lbdold, rfd - rfdold, rbd - rbdold);
        double deltaY = wheelsToYPosition(lfd - lfdold, lbd - lbdold, rfd - rfdold, rbd - rbdold);
        deltaY = deltaY * 0.85602812451;
        x += deltaX * cos(toRadians(-angle)) + deltaY * sin(toRadians(-angle));
        y += -deltaX * sin(toRadians(-angle)) + deltaY * cos(toRadians(-angle));
        heading = angle;
        lfdold = lfd;
        lbdold = lbd;
        rfdold = rfd;
        rbdold = rbd;
    }

    public double wheelsToXPosition(double lfd, double lbd, double rfd, double rbd) {
        return ((lfd + lbd + rfd + rbd) / 4) / encoderToCm;
    }

    public double wheelsToYPosition(double lfd, double lbd, double rfd, double rbd) {
        return ((-lfd + lbd + rfd - rbd) / 4) / encoderToCm;
    }
}
