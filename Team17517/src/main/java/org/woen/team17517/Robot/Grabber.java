package org.woen.team17517.Robot;

import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Grabber {
    UltRobot robot;
    public DcMotorEx pixelMotor;
    // AnalogInput pixelSensorRight;
    // AnalogInput pixelSensorLeft;
    public Servo pixelLeftServo;
    public Servo pixelGrabServo;
    public Servo progibServo;
    public static double voltage;
    double pixelsCount = 0;
    double pixelsCountOld = 0;
    double targetPower = 1;

    int perekid = 0;
    int grabber = 0;

    public static double grabberOpen = 0.30;
    public static double grabberClose = 0;
    public static double perekidStart = 0.90    ;
    public static double perekidFinish = 0.25;

    boolean protectorIteration = false;
    boolean ampsProtection = false;
    ElapsedTime moveTimer = new ElapsedTime();
    ElapsedTime protectTimer = new ElapsedTime();


    public Grabber(UltRobot robot) {
        moveTimer.reset();
        this.robot = robot;

        // pixelSensorLeft = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageLeft");
        //pixelSensorRight = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageRight");
        progibServo = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServo");
        pixelMotor = (DcMotorEx) this.robot.linearOpMode.hardwareMap.dcMotor.get("pixelMotor");
       // pixelLeftServo = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLeft");
        pixelGrabServo = this.robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoRight");

    }

    public void powerPixelMotor(double power){
        pixelMotor.setPower(power * 0.5);
    }

    public void update() {
        double motorCurrent = pixelMotor.getCurrent(AMPS);
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
        // if ((pixelSensorLeft.getVoltage() > voltage || pixelSensorRight.getVoltage() > voltage) && robot.lift.liftPos && !ampsProtection) {
        //     pixelMotor.setPower(targetPower);
        //   robot.lift.liftPos = !robot.lift.liftPos;
        //} else
        //   pixelMotor.setPower(0);
        //robot.linearOpMode.telemetry.addData("motorCurrent", motorCurrent);
    }

    public void enable(boolean motorPowerControll) {
        if (motorPowerControll)
            targetPower = 1;
        else
            targetPower = 0;

    }
   public void positionGrabber(){
        if(grabber == 0){
            pixelGrabServo.setPosition(grabberClose);
            grabber = 1;
        }
        else{
            if(grabber == 1){
                pixelGrabServo.setPosition(grabberOpen);
                grabber = 0;
            }
        }
    }
    public void positionPerekid(){
        if(perekid == 0){
            robot.grabber.progibServo.setPosition(perekidStart);
            perekid = 1;
        }
        else {
            if (perekid == 1) {
                robot.grabber.progibServo.setPosition(perekidFinish);
                perekid = 0;
            }
        }
    }
}
