package org.woen.team17517.Robot;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

@Config
public class Lift {
    public static double SERVO_POS_UP = 0.71;
    public static double SERVO_POS_DOWN = 0.32;
    public DcMotor liftMotor1;
    public DcMotor liftMotor2;
    public DigitalChannel buttonUp;
    public DigitalChannel buttonDown;
    public double power = 0;
    public LiftPosition liftPosition = LiftPosition.ZERO;
    public LiftMode liftMode = LiftMode.AUTO;
    double err1 = 0;
    double err2 = 0;
    boolean servoLiftUp = false;
    boolean servoLiftButtonOld = false;
    UltRobot robot;
    int liftOffset1 = 0;
    int liftOffset2 = 0;
    private final PidRegulator PIDZL1 = new PidRegulator(0.005964, 0, 0);
    private final PidRegulator PIDZL2 = new PidRegulator(0.005964, 0, 0);
    public boolean liftPos = true;
    public Lift(UltRobot robot) {
        this.robot = robot;

        liftMotor1 = this.robot.linearOpMode.hardwareMap.dcMotor.get("motor1");
        liftMotor2 = this.robot.linearOpMode.hardwareMap.dcMotor.get("motor2");
        buttonUp = this.robot.linearOpMode.hardwareMap.digitalChannel.get("top");
        buttonDown = this.robot.linearOpMode.hardwareMap.digitalChannel.get("end");
        liftMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        buttonUp.setMode(DigitalChannel.Mode.INPUT);
        buttonDown.setMode(DigitalChannel.Mode.INPUT);
    }

    public void reset() {
        liftMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPowers(double x) {
        liftMotor1.setPower(x);
        liftMotor2.setPower(x);
    }

    public double encoders() {
        double m1 = liftMotor1.getCurrentPosition();
        double m2 = liftMotor2.getCurrentPosition();
        double m0 = (m1 + m2) / 2;
        return m0;
    }

    private int getPosition1() {
        return liftMotor1.getCurrentPosition() - liftOffset1;
    }

    private int getPosition2() {return liftMotor2.getCurrentPosition() - liftOffset2;}

    public void setPowersLimit(double x) {
        int pos1 = getPosition1();
        int pos2 = getPosition2();
        if (x > 0) {
            if (pos1 > LiftPosition.UP.value) {
                liftMotor1.setPower(0);
            } else {
                liftMotor1.setPower(x);
            }
            if (pos2 > LiftPosition.UP.value) {

                liftMotor2.setPower(0);
            } else {
                liftMotor2.setPower(x);
            }
        } else {
            if (pos1 < LiftPosition.ZERO.value) {
                liftMotor1.setPower(0);
            } else {
                liftMotor1.setPower(x);
            }
            if (pos2 < LiftPosition.ZERO.value) {
                liftMotor2.setPower(0);
            } else {
                liftMotor2.setPower(x);
            }
        }
    }

    public void displayEncoders() {
        robot.linearOpMode.telemetry.addData("lift1", liftMotor1.getCurrentPosition());
        robot.linearOpMode.telemetry.addData("lift2", liftMotor2.getCurrentPosition());
        robot.linearOpMode.telemetry.update();
    }

    public void setMotor(LiftPosition position) {
        liftPosition = position;
        double height = position.value;

        liftMode = LiftMode.AUTO;
        double l1 = liftMotor1.getCurrentPosition();
        double l2 = liftMotor2.getCurrentPosition();
        double motorsY = (l1 + l2) / 2;

        double target1 = height;
        double target2 = height;
        err1 = target1 - l1;
        err2 = target2 - l2;
        PIDZL1.update(motorsY);
        PIDZL2.update(motorsY);

        double t1 = (double) System.currentTimeMillis() / 1000.0;
        double t;
        double tr = 0;

        while (!isAtPosition() && tr < 3 && robot.linearOpMode.opModeIsActive()) {
            t = (double) System.currentTimeMillis() / 1000.0;
            tr = t - t1;
            liftMode = LiftMode.AUTO;
            update();
        }

        liftMotor1.setPower(0);
        liftMotor2.setPower(0);
    }

    public void update() {
        switch (liftMode) {
            case AUTO: {
                    double target1 = liftPosition.value;
                    double target2 = liftPosition.value;
                    double l1 = getPosition1();
                    double l2 = getPosition2();
                    err1 = target1 - l1;
                    err2 = target2 - l2;
                    double poweryl1 = 0.2 + PIDZL1.update(err1);
                    double poweryl2 = 0.2 + PIDZL2.update(err2);
                    liftMotor1.setPower(Range.clip(poweryl1, -0.1, 0.7));
                    liftMotor2.setPower(Range.clip(poweryl2, -0.1, 0.7));
                    if(abs(l1) > 50 && abs(l2) > 50)
                        liftPos = false;
                    else
                        liftPos = true;
                }
                break;
            case MANUALLIMIT:
                setPowersLimit(power);
                break;
            case MANUAL:
                setPowers(power);
                break;
        }
    }

    public boolean isAtPosition() {
        return abs(err1) < 5 && abs(err2) < 5;
    }

    public enum LiftMode {
        AUTO, MANUAL, MANUALLIMIT
    }

    public enum LiftPosition {
        ZERO(0), LOW(780), MIDDLE(1008), UP(1108);
        public int value;

        LiftPosition(int value) {
            this.value = value;
        }
    }
}
