package org.woen.team17517.Robot;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.Range;

@Config
public class Lift implements RobotModule {
    public static double kP = 0.005964;
    public static double kI = 0;
    public static double kD = 0;
    public DcMotor liftMotor;
    public DigitalChannel buttonUp;
    public DigitalChannel buttonDown;
    private double power = 0;
    public LiftPosition targetPosition = LiftPosition.UNKNOWN;
    public LiftMode liftMode = LiftMode.AUTO;
    double err1 = 0;
    UltRobot robot;
    static int liftOffset = -LiftPosition.UP.value;
    private final PidRegulator PIDZL1 = new PidRegulator(kP, kI, kD);
    public boolean liftPos = true;

    public Lift(UltRobot robot) {
        this.robot = robot;
        liftMotor = this.robot.linearOpMode.hardwareMap.dcMotor.get("liftMotor");
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        buttonUp = robot.linearOpMode.hardwareMap.digitalChannel.get("buttonUp");
        buttonDown = robot.linearOpMode.hardwareMap.digitalChannel.get("buttonDown");
        buttonUp.setMode(DigitalChannel.Mode.INPUT);
        buttonDown.setMode(DigitalChannel.Mode.INPUT);
        //resetEncoder();
    }

    private void resetEncoder(){
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public boolean getTopSwitch(){
        return !buttonUp.getState();
    }

    public void reset() {
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void setPower(double x) {
        liftMotor.setPower(x);
    }

    public int getRawPosition(){
        return liftMotor.getCurrentPosition();
    }

    public int getPosition() {
        return getRawPosition() + liftOffset;
    }

    public void setPositionOffset(int liftOffset){
        Lift.liftOffset = liftOffset;
    }

    private void setPowersLimit(double x) {
        int pos = getPosition();
        if (x > 0) {
            if (pos > LiftPosition.UP.value) {
                liftMotor.setPower(0);
            } else {
                liftMotor.setPower(x);
            }
        } else {
            if (pos < LiftPosition.DOWN.value) {
                liftMotor.setPower(0);
            } else {
                liftMotor.setPower(x);
            }
        }
    }

    public void update() {
        if (true)
            return;
        switch (liftMode) {
            case AUTO: {
                double target1 = targetPosition.value;
                double position = getPosition();
                err1 = target1 - position;
                double poweryl1 = 0.2 + PIDZL1.update(err1);
                liftMotor.setPower(Range.clip(poweryl1, -0.1, 0.7));
                if (abs(position) > 50)
                    liftPos = false;
                else
                    liftPos = true;
            }
            break;
            case MANUALLIMIT:
                setPowersLimit(power);
                break;
            case MANUAL:
                setPower(power);
                break;
        }
    }

    public boolean isAtPosition() {
        return abs(err1) < 5;
    }

    public enum LiftMode {
        AUTO, MANUAL, MANUALLIMIT
    }

    public enum LiftPosition {
        DOWN(0), UP(2733), UNKNOWN(0);
        public int value;

        LiftPosition(int value) {
            this.value = value;
        }
    }
}
