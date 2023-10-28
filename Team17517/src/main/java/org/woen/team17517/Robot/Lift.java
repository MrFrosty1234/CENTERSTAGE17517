package org.woen.team17517.Robot;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

@Config
public class Lift {
    public static double kP = 0.005964;
    public static double kI = 0;
    public static double kD = 0;
    public DcMotor liftMotor;
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
    int liftOffset= 0;
    private final PidRegulator PIDZL1 = new PidRegulator(kP, kI, kD);
    public boolean liftPos = true;
    public Lift(UltRobot robot) {
        this.robot = robot;

        liftMotor = this.robot.linearOpMode.hardwareMap.dcMotor.get("motor");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        buttonUp.setMode(DigitalChannel.Mode.INPUT);
        buttonDown.setMode(DigitalChannel.Mode.INPUT);
    }

    public void reset() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPowers(double x) {
        liftMotor.setPower(x);
    }


    private int getPosition() {
        return liftMotor.getCurrentPosition() - liftOffset;
    }


    public void setPowersLimit(double x) {
        int pos = getPosition();
        if (x > 0) {
            if (pos > LiftPosition.UP.value) {
                liftMotor.setPower(0);
            } else {
                liftMotor.setPower(x);
            }
        } else {
            if (pos < LiftPosition.ZERO.value) {
                liftMotor.setPower(0);
            } else {
                liftMotor.setPower(x);
            }
        }
    }

    public void displayEncoders() {
        robot.linearOpMode.telemetry.addData("lift", liftMotor.getCurrentPosition());
        robot.linearOpMode.telemetry.update();
    }

    public void setMotor(LiftPosition position) {
        liftPosition = position;
        double height = position.value;

        liftMode = LiftMode.AUTO;
        double l = liftMotor.getCurrentPosition();
        double motorsY = (l + l) / 2;

        double target1 = height;
        double target2 = height;
        err1 = target1 - l;


        PIDZL1.update(motorsY);

        double t1 = (double) System.currentTimeMillis() / 1000.0;
        double t;
        double tr = 0;

        while (!isAtPosition() && tr < 3 && robot.linearOpMode.opModeIsActive()) {
            t = (double) System.currentTimeMillis() / 1000.0;
            tr = t - t1;
            liftMode = LiftMode.AUTO;
            update();
        }

        liftMotor.setPower(0);
    }

    public void update() {
        switch (liftMode) {
            case AUTO: {
                    double target1 = liftPosition.value;
                    double l = getPosition();
                    err1 = target1 - l;
                    double poweryl1 = 0.2 + PIDZL1.update(err1);
                    liftMotor.setPower(Range.clip(poweryl1, -0.1, 0.7));
                    if(abs(l) > 50)
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
