package org.woen.team17517.RobotModules.Lift;

import static org.woen.team17517.RobotModules.Lift.LiftMode.AUTO;
import static org.woen.team17517.RobotModules.Lift.LiftMode.MANUALLIMIT;
import static org.woen.team17517.RobotModules.Lift.LiftPosition.BACKDROPDOWN;
import static org.woen.team17517.RobotModules.Lift.LiftPosition.DOWN;
import static org.woen.team17517.RobotModules.Lift.LiftPosition.UP;
import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.PID;
import org.woen.team17517.Service.RobotModule;


@Config
public class Lift implements RobotModule {
    private DcMotorEx liftMotor;
    private DigitalChannel buttonUp;
    private DigitalChannel buttonDown;
    private double voltage;
    private LiftPosition targetPosition = DOWN;
    private double targetSpeed = 0;
    public double getTargetSpeed() {return targetSpeed;}
    public double getSpeed() {return speed;}
    public LiftPosition getTargetPosition(){
        return targetPosition;
    }
    private void setTargetPosition(LiftPosition targetPosition){this.targetPosition = targetPosition;}
    private LiftMode liftMode = AUTO;
    public LiftMode getLiftMode(){
        return liftMode;
    }
    UltRobot robot;
    private boolean liftAtTarget = true;
    public static double kp = 0;
    public static double ki = 0;
    public static double kd = 0;
    public static double kg = 0;
    public static double maxI  = 0;

    public static double velKp = 0;
    public static double velKg = 0;
    public static double velKi = 0;
    public static double velKs = 0;
    public static double velKd = 0;
    public static double velMaxI = 0;
    PID pid = new PID(kp,ki,kd,0,maxI,kg);
    PID pidVelocity = new PID(velKp,velKi,velKd,velKs,velMaxI,velKg);
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
    Button down = new Button();
    public boolean getUpSwitch(){
        return false;
    }
    public boolean getDownSwitch(){return down.update(buttonDown.getState());}
    public void setPower(double target) {
        double power = pidVelocity.pid(target,liftMotor.getVelocity(),voltage);
        liftMotor.setPower(power);
    }

    public void moveUP(){
        setTargetPosition(UP);
        setLiftMode(AUTO);
    }
    public void moveDown(){
        setTargetPosition(DOWN);
        setLiftMode(AUTO);
    }
    public void moveBackDropDown(){
        setTargetPosition(BACKDROPDOWN);
        setLiftMode(AUTO);
    }

    private int encoderError  = 0;
    private int encoderPosition = 0;
    public int getPosition() {return encoderPosition;}
    private int cleanPosition = 0;
    public int getCleanPosition() {return cleanPosition;}
    private double speed = 0;
    public void updatePosition(){
        cleanPosition = liftMotor.getCurrentPosition();
        encoderPosition = cleanPosition - encoderError;
        if (getUpSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.UP.get();
        }if (getDownSwitch()){
            encoderError = liftMotor.getCurrentPosition() - DOWN.get();
        }
        speed = liftMotor.getVelocity();
    }

    public double getPidPosInputForPid() {
        return pidPosInputForPid;
    }

    private double pidPosInputForPid = 0;
    public void update(){
        updatePosition();
        pid.setCoeficent(kp, ki, kd, 0, maxI, kg);
        pidVelocity.setCoeficent(velKp,velKi,velKd,velKs,velMaxI,velKg);
        voltage = robot.voltageSensorPoint.getVol();
        switch (liftMode) {
            case AUTO:
                pidPosInputForPid = pid.pid(targetPosition.get(),getPosition(),voltage);
                liftAtTarget = abs(targetPosition.get()-getPosition())<5;
                break;
            case MANUALLIMIT:
                pidPosInputForPid = pidVelocity.pid(targetSpeed,speed,voltage);
                liftAtTarget = true;
                break;
        }

        setPower(pidPosInputForPid);
    }
    public void setSpeed(double speed){
        targetSpeed = speed;
        setLiftMode(MANUALLIMIT);
    }
    public double getSpeedPercent(double percent){
        return percent*maxSpeed;
    }
    private double maxSpeed = 2400;
    @Override
    public boolean isAtPosition() {
        return liftAtTarget;
    }
}
