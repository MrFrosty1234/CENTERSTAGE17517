package org.woen.team17517.RobotModules.Transport.Lift;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
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
    public boolean liftAtTaget = false;
    private LiftPosition position;
    public void setLiftMode(LiftMode mode){
        liftMode = mode;
    }
    public Lift(UltRobot robot) {
        this.robot = robot;
        liftMotor = robot.devices.liftMotor;
        buttonUp = robot.devices.buttonUp;
        buttonDown = robot.devices.buttonDown;
    }
    public boolean getUpSwitch(){
        return buttonUp.getState();
    }
    public boolean getDownSwitch(){return buttonDown.getState();}


    public void setPower(double x) {
        liftMotor.setPower(x);
    }

    private int encoderError  = 0;
    public void moveUP(){
        this.targetPosition = LiftPosition.UP;
        setLiftMode(LiftMode.AUTO);
    }
    public void moveDown(){
        this.targetPosition = LiftPosition.DOWN;
        setLiftMode(LiftMode.AUTO);
    }
    private int encoderPosition = 0;

    public int getEncoderPosition() {
        return encoderPosition;
    }

    private int cleanPosition = 0;

    public int getCleanPosition() {return cleanPosition;}

    public void updateEncoderPosition(){
        cleanPosition = liftMotor.getCurrentPosition();
        encoderPosition = cleanPosition -encoderError;
        if (getUpSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.UP.value;
        }if (getDownSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.DOWN.value;
        }
    }
    public void update() {
        updateEncoderPosition();
        updatePosition();

        double liftPower = 0;
        double liftGravityPower = 0.1;
        double liftMovePower = 0.8;
        switch (liftMode){
            case AUTO:
                switch (targetPosition) {
                    case UP:
                        liftAtTaget = targetPosition.value == getEncoderPosition();
                        liftPower = liftAtTaget ? liftGravityPower : liftMovePower;
                        setPower(liftPower);
                        break;
                    case DOWN:
                        liftAtTaget = getEncoderPosition() <= LiftPosition.DOWN.value;
                        liftPower = liftAtTaget ? 0 : -liftMovePower;
                        robot.linearOpMode.telemetry.addData("is",liftAtTaget);
                        robot.linearOpMode.telemetry.update();
                        setPower(liftPower);
                        break;
                    case FORAUTONOM:
                        liftAtTaget =  getEncoderPosition() == LiftPosition.FORAUTONOM.value;
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
                if(manualTargetUp && getUpSwitch()){
                    liftPower = liftMovePower;
                    setPower(liftPower);
                }else if (manualTargetDown){
                    liftPower = -liftMovePower;
                    setPower(liftPower);
                }else {
                    liftPower = liftGravityPower;
                    setPower(liftPower);
                }
                liftAtTaget = true;
            break;
        }

    }
    private void updatePosition(){
        if(!liftAtTaget) position = LiftPosition.UNKNOWN;
        else             position = targetPosition;
    }
    public  LiftPosition getPosition(){
        return position;
    }
    public void setManualTargetUp(){
        setLiftMode(LiftMode.MANUALLIMIT);
        manualTargetUp = true;
        manualTargetDown = false;
    }
    public void setStopManualTarget(){
        setLiftMode(LiftMode.MANUALLIMIT);
        manualTargetUp = false;
        manualTargetDown = false;
    }
    private boolean manualTargetUp = false;
    public void setManualTargetDown(){
        setLiftMode(LiftMode.MANUALLIMIT);
        manualTargetDown = true;
        manualTargetUp = false;
    }
    private  boolean manualTargetDown = false;
    @Override
    public boolean isAtPosition() {
        return liftAtTaget;
    }

}
