package org.woen.team17517.Robot;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.signum;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


@Config
public class DriveTrain{

    public static double mechanumSideKf = 1.5;
    public static double maxPower = 1.0;
    public static double minXErr = 3.0;
    public static double minYErr = 3.0;
    public static double minHeadingErr = 2.0;
    public static double movingTime = 5;
    public static double kPdrive = 0.045;
    public static double kIdrive = 0.025;
    public static double kDdrive = 0;
    public static double kProtation = 0.04;
    public static double kIrotation = 0.01;
    public static double kDrotation = 0;
    public static double radiusWheelCm = 9.8;
    public static double encodersWithoutReductor = 24;
    public static double motorReductor = 20;
    public static double timeForAcceliration = 0.5;
    public static double powerXMin = -0.75;
    public static double powerXMax = 0.75;
    public static double powerYMin = -0.75;
    public static double powerYMax = 0.75;
    public static double powerHeadingMin = -0.55;
    public static double powerHeadingMax =  0.55;
    public double xError = 0;
    public double yError = 0;
    public double headingError = 0;


    double gyro;
    double encoderToCm = encodersWithoutReductor * motorReductor / (radiusWheelCm * PI);
    double headingTargetGlobal = 0;
    UltRobot robot;
    public final DcMotor left_front_drive;
    public final DcMotor left_back_drive;
    public final DcMotor right_front_drive;
    public final DcMotor right_back_drive;
    private final PidRegulator pidX = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidY = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidH = new PidRegulator(kProtation, kIrotation, kDrotation);
    private final PidRegulator pidFieldX = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidFieldY = new PidRegulator(kPdrive, kIdrive, kDdrive);



    public DriveTrain(UltRobot robot) {
        this.robot = robot;

        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");
        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setDirection(DcMotor.Direction.REVERSE);
        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_back_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_front_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reset();
    }

    public void reset() {
        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }


    public void setPowers(double x, double y, double z) {
        y *= mechanumSideKf;
        double leftFrontMotorPower = x - y - z;
        double rightFrontMotorPower = x + y + z;
        double leftRearMotorPower = x + y - z;
        double rightRearMotorPower = x - y + z;
        double biggestPower = max(max(abs(leftFrontMotorPower), abs(leftRearMotorPower)), max(abs(rightFrontMotorPower), abs(rightRearMotorPower)));

        if (biggestPower > maxPower) {
            leftFrontMotorPower /= biggestPower;
            leftRearMotorPower /= biggestPower;
            rightFrontMotorPower /= biggestPower;
            rightRearMotorPower /= biggestPower;
        }

        left_front_drive.setPower(leftFrontMotorPower);
        left_back_drive.setPower(leftRearMotorPower);
        right_front_drive.setPower(rightFrontMotorPower);
        right_back_drive.setPower(rightRearMotorPower);
    }

    public void displayEncoders() {
        robot.linearOpMode.telemetry.addData("lfd", left_front_drive.getCurrentPosition());
        robot.linearOpMode.telemetry.addData("lrd", left_back_drive.getCurrentPosition());
        robot.linearOpMode.telemetry.addData("rfd", right_front_drive.getCurrentPosition());
        robot.linearOpMode.telemetry.addData("rrd", right_back_drive.getCurrentPosition());
    }

    double constantVelocityMotion(double start, double time, double speed, double finish) {
        double dist = finish - start;
        if (finish < speed * time + start)
            return finish;
        if (start > finish)
            return speed * time * (-1) + start;
        else
            return speed * time + start;
    }
    private double angleTransform(double angle){
        while (abs(angle) > 180) {
            angle -= 360 * signum(angle);
        }
        return angle;
    }

    public void moveField(double xTarget, double yTarget, double headingTarget) {
        pidFieldX.reset();
        pidFieldY.reset();
        pidH.reset();
        xError = xTarget - robot.odometry.x;
        yError = yTarget - robot.odometry.y;
        headingTargetGlobal = headingTarget;
        headingError = headingTargetGlobal - robot.odometry.heading;

        angleTransform(headingError);

        pidFieldX.update(xError);
        pidFieldY.update(yError);
        pidH.update(headingError);

        ElapsedTime moveTimer = new ElapsedTime();
        moveTimer.reset();

        double moveTime = 0;

        while (((abs(xError)) > minXErr || (abs(yError)) > minYErr || (abs(headingError)) > minHeadingErr) && moveTime < movingTime && robot.linearOpMode.opModeIsActive()) { //TODO parameters
            double timeNow = moveTimer.seconds();
            moveTime = timeNow;

            xError = xTarget - robot.odometry.x;
            yError = yTarget - robot.odometry.y;
            headingError = headingTargetGlobal - robot.odometry.heading;


            angleTransform(headingError);
            robot.allUpdate();
            double powerx = pidFieldX.update(xError);
            double powery = pidFieldY.update(yError);
            double powerz = pidH.update(headingError);
            if (moveTime < timeForAcceliration) {
                powerx = moveTime / timeForAcceliration * powerx;
                powery = moveTime / timeForAcceliration * powery;
                powerz = moveTime / timeForAcceliration * powerz;
            }
            setPowersField(Range.clip(powerx, powerXMin, powerXMax), Range.clip(powery, powerYMin, powerYMax), Range.clip(powerz, powerHeadingMin, powerHeadingMax));
            robot.linearOpMode.telemetry.addData("t", moveTime);
            robot.linearOpMode.telemetry.update();
        }
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);
    }

    public void setPowersField(double x, double y, double heading) {
        double angle = robot.odometry.heading;
        double powersX = x * cos(toRadians(angle)) + y * sin(toRadians(angle));
        double powersY = -x * sin(toRadians(angle)) + y * cos(toRadians(angle));
        setPowers(powersX, powersY, heading);
    }
}

