package org.woen.team17517.RobotModules.Intake.Lift;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.PID;
import org.woen.team17517.Service.RobotModule;


@Config
public class Lift implements RobotModule {
    private final DcMotorEx liftMotor;
    public DigitalChannel buttonDown;
    private double voltage;
    private LiftPosition targetPosition = LiftPosition.DOWN;
    public LiftPosition getTargetPosition(){
        return targetPosition;
    }
    private void setTargetPosition(LiftPosition targetPosition){this.targetPosition = targetPosition;}
    UltRobot robot;
    private boolean liftAtTarget = true;
    public static double kp = 0.01;
    public static double ki = 0;
    public static double kd = 0.1;
    public static double kg = 0.093;
    public static double maxI  = 0;
    PID pid = new PID(kp,ki,kd,0,maxI,kg);
    public Lift(UltRobot robot) {
        this.robot = robot;
        liftMotor = robot.hardware.intakeAndLiftMotors.liftMotor;
        buttonDown = robot.hardware.sensors.buttonDown;
        encoderError = liftMotor.getCurrentPosition()-LiftPosition.UP.get();
        voltage = 12;
    }
    Button down = new Button();
    public boolean getUpSwitch(){
        return false;
    }
    public boolean getDownSwitch(){return down.update(buttonDown.getState());}
    public void setPower(double target) {
        liftMotor.setPower(target);
    }

    public void moveUP(){
        setTargetPosition(LiftPosition.UP);
    }
    public void moveDown(){
        setTargetPosition(LiftPosition.DOWN);
    }
    public void moveBackDropDown(){
        setTargetPosition(LiftPosition.BACKDROPDOWN);
    }
    public void move(LiftPosition pos){setTargetPosition(pos);}
    public void moveToMiddle(){
        setTargetPosition(LiftPosition.MIDDLE);
    }

    private int encoderError;
    private int encoderPosition = LiftPosition.DOWN.get();
    public int getPosition() {return encoderPosition;}
    private int cleanPosition = 0;
    public int getCleanPosition() {return cleanPosition;}
    public void updatePosition(){
        cleanPosition = liftMotor.getCurrentPosition();
        encoderPosition = cleanPosition - encoderError;
        if (getUpSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.UP.get();
        }
        if (getDownSwitch()){
            encoderError = liftMotor.getCurrentPosition() - LiftPosition.DOWN.get();
        }
    }

    public double getPower() {
        return power;
    }

    private double power = 0;
    public void update(){
        updatePosition();
        pid.setCoeficent(kp, ki, kd, 0, maxI, kg);
        voltage = robot.voltageSensorPoint.getVol();
        if(getPosition()>-10) {
            if (targetPosition != LiftPosition.DOWN) {
                power = pid.pid(targetPosition.get(), getPosition(), voltage);
                liftAtTarget = abs(targetPosition.get() - getPosition()) < 15;
            } else {
                liftAtTarget = buttonDown.getState();
                power = liftAtTarget ? 0 : -0.4;
            }
        }else{
            power = 0.1;
        }
        setPower(power);
    }
    public double getCurrent(){
        return liftMotor.getCurrent(CurrentUnit.AMPS);
    }
    @Override
    public boolean isAtPosition() {
        return liftAtTarget;
    }
}
