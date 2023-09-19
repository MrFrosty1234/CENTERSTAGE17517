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
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Config
public class DriveTrain {
    public static double kPdrive = 0.045;
    public static double kIdrive = 0.025;
    public static double kDdrive = 0;
    public static double kProtation = 0.04;
    public static double kIrotation = 0.01;
    public static double kDrotation = 0;
    BNO055IMU gyro;
    double encoderToCm = 24 * 20 / (9.8 * PI); //TODO Parameters
    double headingTargetGlobal = 0;
    Robot robot;
    private final DcMotor left_front_drive;
    private final DcMotor left_back_drive;
    private final DcMotor right_front_drive;
    private final DcMotor right_back_drive;
    private final PidRegulator pidX = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidY = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidH = new PidRegulator(kProtation, kIrotation, kDrotation);
    private final PidRegulator pidFieldX = new PidRegulator(kPdrive, kIdrive, kDdrive);
    private final PidRegulator pidFieldY = new PidRegulator(kPdrive, kIdrive, kDdrive);

    public DriveTrain(Robot robot) {
        this.robot = robot;
        gyro = robot.linearOpMode.hardwareMap.get(BNO055IMU.class, "imu");

        gyro.initialize(new BNO055IMU.Parameters());
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");
        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setDirection(DcMotor.Direction.FORWARD);
        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.REVERSE);
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
        y *= 1.5; //TODO parameters
        double leftFrontMotorPower = x - y - z;
        double rightFrontMotorPower = x + y + z;
        double leftRearMotorPower = x + y - z;
        double rightRearMotorPower = x - y + z;
        double biggestPower = max(max(abs(leftFrontMotorPower), abs(leftRearMotorPower)), max(abs(rightFrontMotorPower), abs(rightRearMotorPower)));

        if (biggestPower > 1.0) {
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

    public void moveXYH(double xTarget, double yTarget, double headingTarget) {
        double lfd = left_front_drive.getCurrentPosition();
        double lbd = left_back_drive.getCurrentPosition();
        double rfd = right_front_drive.getCurrentPosition();
        double rbd = right_back_drive.getCurrentPosition();

        double motorsX = robot.odometry.wheelsToXPosition(lfd, lbd, rfd, rbd);
        double motorsY = robot.odometry.wheelsToYPosition(lfd, lbd, rfd, rbd);
        double xError = xTarget - motorsX;
        double yError = yTarget - motorsY;

        headingTargetGlobal += headingTarget;
        double headingError = headingTargetGlobal - gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

        //TODO Separate function
        while (abs(headingError) > 180) {
            headingError -= 360 * signum(headingError);
        }

        pidX.update(xError);
        pidY.update(yError);
        pidH.update(headingError);

        double tStart = System.currentTimeMillis() / 1000.0; //TODO ElapsedTime
        double moveTime = 0;

        while (((abs(xError)) > 75 || (abs(yError)) > 75 || (abs(headingError)) > 4) && moveTime < 5 && robot.linearOpMode.opModeIsActive()) { //TODO Parameters
            double timeNow = System.currentTimeMillis() / 1000.0;
            moveTime = timeNow - tStart;
            lfd = left_front_drive.getCurrentPosition();
            lbd = left_back_drive.getCurrentPosition();
            rfd = right_front_drive.getCurrentPosition();
            rbd = right_back_drive.getCurrentPosition();

            motorsX = robot.odometry.wheelsToXPosition(lfd, lbd, rfd, rbd);
            motorsY = robot.odometry.wheelsToYPosition(lfd, lbd, rfd, rbd);
            xError = xTarget - motorsX;
            yError = yTarget - motorsY;

            double heading = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            headingError = headingTargetGlobal - heading;

            //TODO Separate function
            while (abs(headingError) > 180) {
                headingError -= 360 * signum(headingError);
            }

            double powerx = pidX.update(xError);
            double powery = pidY.update(yError);
            double powerz = pidH.update(headingError);

            if (timeNow < 0.5) { //TODO Parameters
                powerx = timeNow / 500 * powerx;
                powery = timeNow / 500 * powery;
                powerz = timeNow / 500 * powerz;
            }
            setPowers(powerx, powery, powerz);
        }
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);

        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void moveField(double xTarget, double yTarget, double headingTarget) {
        pidFieldX.reset();
        pidFieldY.reset();
        pidH.reset();
        double xError = xTarget - robot.odometry.x;
        double yError = yTarget - robot.odometry.y;
        headingTargetGlobal = headingTarget;
        double headingError = headingTargetGlobal - robot.odometry.heading;

        while (abs(headingError) > 180) { //TODO Separate function
            headingError -= 360 * signum(headingError);
        }

        pidFieldX.update(xError);
        pidFieldY.update(yError);
        pidH.update(headingError);
        double timeStart = System.currentTimeMillis() / 1000.0; //TODO ElapsedTime
        double moveTime = 0;

        while (((abs(xError)) > 3.0 || (abs(yError)) > 3.0 || (abs(headingError)) > 2.0) && moveTime < 5 && robot.linearOpMode.opModeIsActive()) { //TODO parameters
            double timeNow = System.currentTimeMillis() / 1000.0;
            moveTime = timeNow - timeStart;

            xError = xTarget - robot.odometry.x;
            yError = yTarget - robot.odometry.y;
            headingError = headingTargetGlobal - robot.odometry.heading;

            //TODO separate function
            while (abs(headingError) > 180) {
                headingError -= 360 * signum(headingError);
            }

            robot.allUpdate();
            double powerx = pidFieldX.update(xError);
            double powery = pidFieldY.update(yError);
            double powerz = pidH.update(headingError);
            if (moveTime < 0.5) {
                powerx = moveTime / 0.5 * powerx;
                powery = moveTime / 0.5 * powery;
                powerz = moveTime / 0.5 * powerz;
            }
            setPowersField(Range.clip(powerx, -0.75, 0.75), Range.clip(powery, -0.75, 0.75), Range.clip(powerz, -0.55, 0.55));
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

