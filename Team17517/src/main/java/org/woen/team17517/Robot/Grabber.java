package org.woen.team17517.Robot;

import static org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit.AMPS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

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


    public Grabber(UltRobot robot) {
        this.robot = robot;
        pixelSensorLeft = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageLeft");
        pixelSensorRight = this.robot.linearOpMode.hardwareMap.analogInput.get("pixelStorageRight");
        pixelMotor = (DcMotorEx) this.robot.linearOpMode.hardwareMap.dcMotor.get("pixelMotor");
    }


    public void update() {
        double motorCurrent = pixelMotor.getCurrent(AMPS);
        pixelsCountOld = pixelsCount;
            if ((pixelSensorLeft.getVoltage() > voltage || pixelSensorRight.getVoltage() > voltage) && robot.lift.liftPos) {
                pixelMotor.setPower(targetPower);
                robot.lift.liftPos = !robot.lift.liftPos;
            } else
                pixelMotor.setPower(0);
    }
    public void enable(boolean motorPowerControll){
        if(motorPowerControll)
            targetPower = 1;
        else
            targetPower = 0;
    }
}
