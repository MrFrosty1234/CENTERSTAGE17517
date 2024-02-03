package org.woen.team17517.RobotModules.Lift;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;


@Config
public class Lift implements RobotModule {
    public DcMotor liftMotor;
    public DigitalChannel buttonUp;
    public DigitalChannel buttonDown;
    private LiftPosition targetPosition = LiftPosition.UNKNOWN;
    public LiftPosition getTargetPosition(){
        return targetPosition;
    }
    public LiftMode liftMode = LiftMode.AUTO;
    UltRobot robot;
    private int liftOffset = -LiftPosition.UP.value;
    public boolean liftAtTaget = false;
    public void setLiftMode(LiftMode mode){
        liftMode = mode;
    }
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

    private double encoderError  = 0;
    public void setPositionOffset(int liftOffset){
        this.liftOffset = liftOffset;
    }
    public void moveUP(){
        this.targetPosition = LiftPosition.UP;
    }
    public void moveDown(){
        this.targetPosition =LiftPosition.DOWN;
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
    private double encoderPosition = 0;

    public double getEncoderPosition() {
        return encoderPosition;
    }

    public void update() {
        double liftPower = 0;
        double liftGravityPower = 0.1;
        double liftMovePower = 1;
        encoderPosition = liftMotor.getCurrentPosition()-encoderError;
        if (getTopSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.UP.value;
        }
        if (getTopSwitch())
            setPositionOffset(LiftPosition.UP.value - getRawPosition());
        switch (liftMode){
            case AUTO:
                switch (targetPosition) {
                    case UP:
                        liftAtTaget = getTopSwitch();
                        liftPower = liftAtTaget ? liftGravityPower : liftMovePower;
                        setPower(liftPower);
                        break;
                    case DOWN:
                        liftAtTaget = getPosition() <= LiftPosition.DOWN.value;
                        liftPower = liftAtTaget ? 0 : -liftMovePower;
                        setPower(liftPower);
                        break;
                    case FORAUTONOM:
                        if (getPosition() == LiftPosition.FORAUTONOM.value)
                            liftAtTaget = true;
                        else
                            liftAtTaget = false;
                        liftPower = liftAtTaget ? liftGravityPower : liftMovePower;
                        setPower(liftPower);
                        break;
                    default:
                        liftAtTaget = true;
                        liftPower = 0;
                        setPower(liftPower);
                        break;

                }
            break;
            case  MANUALLIMIT:
                if(manualTargetUp && getTopSwitch()){
                    setPower(liftMovePower);
                }else if (manualTargetDown){
                    setPower(0);
                }else{
                    setPower(liftGravityPower);
                }

            break;
        }

    }
    public void setManualTargetUp(boolean manualTargetUp){
        this.manualTargetUp = manualTargetUp;
    }
    private boolean manualTargetUp = false;
    public void setManualTargetDown(boolean manualTargetDown){
        this.manualTargetDown = manualTargetDown;
    }
    private  boolean manualTargetDown = false;
    @Override
    public boolean isAtPosition() {
        return liftAtTaget;
    }

}
