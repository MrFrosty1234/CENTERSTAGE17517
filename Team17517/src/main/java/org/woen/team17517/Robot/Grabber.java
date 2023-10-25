package org.woen.team17517.Robot;

import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Config
public class Grabber {
    UltRobot robot;
    DcMotorEx pixelMotor;
    AnalogInput pixelSensorRight;
    AnalogInput pixelSensorLeft;
    public static double voltage;
    double pixelsCount = 0;
    double pixelsCountOld = 0;
    double targetPower = 1;

    boolean protectorIteration = false;
    boolean ampsProtection = false;
    ElapsedTime moveTimer = new ElapsedTime();
    ElapsedTime protectTimer = new ElapsedTime();


    public Grabber(UltRobot robot) {
        moveTimer.reset();
        this.robot = robot;

        pixelSensorLeft = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageLeft");
        pixelSensorRight = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageRight");
        pixelMotor = (DcMotorEx) this.robot.linearOpMode.hardwareMap.dcMotor.get("pixelMotor");
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
        if ((pixelSensorLeft.getVoltage() > voltage || pixelSensorRight.getVoltage() > voltage) && robot.lift.liftPos && !ampsProtection) {
            pixelMotor.setPower(targetPower);
            robot.lift.liftPos = !robot.lift.liftPos;
        } else
            pixelMotor.setPower(0);
        robot.linearOpMode.telemetry.addData("motorCurrent", motorCurrent);
    }

    public void enable(boolean motorPowerControll) {
        if (motorPowerControll)
            targetPower = 1;
        else
            targetPower = 0;
    }
}
