package org.woen.team17517.NotUsedCode;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.Transport.Grabber.GrabberMode;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

@Config
public class Grabber implements RobotModule {
    UltRobot robot;
    public DcMotorEx pixelMotor;
    public AnalogInput pixelSensorRight;
    public AnalogInput pixelSensorLeft;
    public Servo pixelServoRight;
    public Servo progibServo;
    public Servo pixelServoLeft;

    public static double voltage;
    public double pixelsCount = 0;
    double pixelsCountOld = 0;
    double targetPower = 1;
    public double pixelLeftSensorVoltage = 3.3;
    public double pixelRightSensorVoltage = 3.3;
    public boolean pixelIn = false;
    public boolean positionGrabber = false;

    public GrabberMode grabberMode = GrabberMode.MANUALMODE;
    PixelsPosition pixelsPosition = PixelsPosition.AUTONOM;
    int perekid = 0;
    int grabber = 0;

    public static double grabberOpenLeft = 0.7;
    public static double grabberCloseLeft = 0.1;
    public static double grabberOpenRight = 0.1;
    public static double grabberCloseRight = 0.7;

    public static double perekidStartDown = 0.875;
    public static double perekidStart = 0.85;
    public static double perekidFinish = 0.3;

    boolean protectorIteration = false;
    boolean ampsProtection = false;
    ElapsedTime moveTimer = new ElapsedTime();
    ElapsedTime protectTimer = new ElapsedTime();


    public Grabber(UltRobot robot) {
        moveTimer.reset();
        this.robot = robot;

        pixelSensorLeft = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageLeft");
        pixelSensorRight = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageRight");
        progibServo = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLift");
        pixelMotor = (DcMotorEx) this.robot.linearOpMode.hardwareMap.dcMotor.get("intakeMotor");
        pixelServoRight = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoRight");
        pixelServoLeft = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLeft");

    }

    public void powerPixelMotor(double power) {
        pixelMotor.setPower(power * 0.5);
    }

    public void graberToClose() {
        positionGrabber = false;
    }

    public void graberToOpen() {
        positionGrabber = true;
    }

    public void update() {
        pixelLeftSensorVoltage = pixelSensorLeft.getVoltage();
        pixelRightSensorVoltage = pixelSensorRight.getVoltage();
        double motorCurrent = pixelMotor.getCurrent(CurrentUnit.AMPS);

        switch (grabberMode) {
            case FULLPROTECTION: {

                if (moveTimer.time() < 0.5) {
                    moveTimer.reset();
                    protectTimer.reset();
                }
                if (motorCurrent < 1) {
                    ampsProtection = false;
                    protectorIteration = false;
                } else {
                    ampsProtection = true;
                    protectorIteration = true;
                }
                if (ampsProtection && protectTimer.time() < 1 && protectorIteration) {
                    pixelMotor.setPower(-1);
                    protectorIteration = false;
                }

                pixelsCountOld = pixelsCount;

                if((pixelSensorLeft.getVoltage() > voltage || pixelSensorRight.getVoltage() > voltage) && robot.lift.liftAtTaget && !ampsProtection) {
                    pixelMotor.setPower(targetPower);
                    robot.lift.liftAtTaget = !robot.lift.liftAtTaget;
                } else
                    pixelMotor.setPower(0);
                if ((pixelSensorLeft.getVoltage() > voltage || pixelSensorRight.getVoltage() > voltage) && robot.lift.liftAtTaget && !ampsProtection) {
                    pixelMotor.setPower(targetPower);
                    robot.lift.liftAtTaget = !robot.lift.liftAtTaget;
                } else
                    pixelMotor.setPower(0);

            }
            case MANUALMODE: {

            }


            switch (pixelsPosition) {
                case AUTONOM: {
                    if (pixelRightSensorVoltage > 1 && pixelLeftSensorVoltage > 1 && positionGrabber) {
                        closeGraber();
                    }
                    if (!positionGrabber) {
                        openGraber();
                    }
                }
                case MANUAL: {
                    if (positionGrabber)
                        closeGraber();
                    if (!positionGrabber)
                        openGraber();
                }
            }

        }
    }

    public void enable(boolean motorPowerControll) {
        if (motorPowerControll)
            targetPower = 1;
        else
            targetPower = 0;

    }

    public void openGraber() {
        pixelServoRight.setPosition(grabberOpenRight);
        pixelServoLeft.setPosition(grabberOpenLeft);
    }

    public void closeGraber() {
        pixelServoRight.setPosition(grabberCloseRight);
        pixelServoLeft.setPosition(grabberCloseLeft);
    }

    public void openRightGraber(){
        pixelServoRight.setPosition(grabberOpenRight);
    }
    public void openLeftGraber(){
        pixelServoLeft.setPosition(grabberOpenLeft);
    }
    public void closeRightGraber(){
        pixelServoRight.setPosition(grabberCloseRight);
    }
    public void closeLeftGraber(){
        pixelServoLeft.setPosition(grabberCloseLeft);
    }

    public void perekidLiftSafe() {
        this.progibServo.setPosition(perekidStartDown);
    }


    public void perekidStart() {
        progibServo.setPosition(perekidStart);
    }

    public void perekidFinish() {
        progibServo.setPosition(perekidFinish);
    }

    public enum PerekidPosition {
        START, FINISH, LIFT_SAFE

    }

    public void setPerekidPosition(PerekidPosition perekidPosition){
        switch (perekidPosition){
            case START:
                perekidStart();
                break;
            case FINISH:
                perekidFinish();
                break;
            case LIFT_SAFE:
                perekidLiftSafe();
                break;
        }
    }

    public void positionPerekid() {
        if (perekid == 0) {
            perekidStart();
            perekid = 1;
        } else {
            if (perekid == 1) {
                perekidFinish();
                perekid = 0;
            }
        }
    }


    public boolean pixelЕxamination() {
        if (pixelLeftSensorVoltage > 1 && pixelRightSensorVoltage > 1) {
            pixelIn = true;
        } else {
            pixelIn = false;
        }
        return pixelIn;
    }



    public enum PixelsPosition {
        AUTONOM, MANUAL;
    }
}
