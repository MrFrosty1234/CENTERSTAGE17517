package org.woen.team17517.RobotModules.Lift;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.PIDMethod;
import org.woen.team17517.Service.RobotModule;


@Config
public class Lift implements RobotModule {
    private DcMotor liftMotor;
    private DigitalChannel buttonUp;
    private DigitalChannel buttonDown;
    private double voltage;
    private LiftPosition targetPosition = LiftPosition.DOWN;
    public LiftPosition getTargetPosition(){
        return targetPosition;
    }
    private LiftMode liftMode = LiftMode.AUTO;
    public LiftMode getLiftMode(){
        return liftMode;
    }
    UltRobot robot;
    private boolean liftAtTaget = false;
    private LiftPosition position = LiftPosition.DOWN;
    public static double kp;
    public static double ki;
    public static double ks = 0;
    public static double kd = 0;
    public static double maxI;
    PIDMethod pid = new PIDMethod(kp,ki,kd,ks,maxI);
    public void setLiftMode(LiftMode mode){
        liftMode = mode;
    }
    public Lift(UltRobot robot) {
        this.robot = robot;
        liftMotor = robot.hardware.intakeAndLiftMotors.liftMotor;
        buttonUp = robot.hardware.sensors.buttonUp;
        buttonDown = robot.hardware.sensors.buttonDown;
        voltage = 12;
    }
    Button up   = new Button();
    Button down = new Button();
    public boolean getUpSwitch(){
        return false;
    }//up.update(buttonUp.getState();
    public boolean getDownSwitch(){return down.update(buttonDown.getState());}

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
    public void moveToYellowPixel(){
        this.targetPosition = LiftPosition.YELLOWPIXEL;
        setLiftMode(LiftMode.AUTO);
    }
    public void moveToWhitePixel(){
        this.targetPosition = LiftPosition.WHITEPIXEL;
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
        encoderPosition = cleanPosition - encoderError;
        if (getUpSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.UP.value;
        }if (getDownSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.DOWN.value;
        }
    }
    public void update() {
        updateEncoderPosition();
        updatePosition();

        pid.setCoefficent(kp,ki,kd,ks,maxI);
        voltage = robot.voltageSensorPoint.getVol();

        double liftPower = 0;
        double liftGravityPower = 0.05;
        double liftMovePower = 0.8;
        switch (liftMode){
            case AUTO:
                switch (targetPosition) {
                    case UP:
                        liftAtTaget =  getEncoderPosition() > LiftPosition.UP.value;
                        liftPower = liftAtTaget ? liftGravityPower : liftMovePower;
                        setPower(liftPower);
                        break;
                    case DOWN:
                        liftAtTaget = getDownSwitch() || getEncoderPosition() < LiftPosition.DOWN.value;
                        liftPower = liftAtTaget ? 0 : -liftMovePower*0.1;
                        setPower(liftPower);
                        break;
                    case FORAUTONOM:
                        liftAtTaget =  getEncoderPosition() == LiftPosition.FORAUTONOM.value;
                        liftPower = liftAtTaget ? liftGravityPower : liftMovePower;
                        setPower(liftPower);
                        break;
                    case YELLOWPIXEL:
                        liftAtTaget = Math.abs(LiftPosition.YELLOWPIXEL.value - getEncoderPosition()) < 25;
                        liftPower = pid.PID(LiftPosition.YELLOWPIXEL.value,getEncoderPosition(),voltage);
                        setPower(liftPower);
                        break;
                    case WHITEPIXEL:
                        liftAtTaget = Math.abs(LiftPosition.WHITEPIXEL.value - getEncoderPosition()) < 25;
                        liftPower = pid.PID(LiftPosition.WHITEPIXEL.value,getEncoderPosition(),voltage);
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
                if(manualTargetUp && !getUpSwitch()){
                    liftPower = liftMovePower*0.6;
                    setPower(liftPower);
                }else if (manualTargetDown && !getDownSwitch()){
                    liftPower = -liftMovePower*0.1;
                    setPower(liftPower);
                }else{
                    if(!getDownSwitch()) liftPower = liftGravityPower;
                    else liftPower = 0;
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
        return true;//liftAtTaget;
    }

}
