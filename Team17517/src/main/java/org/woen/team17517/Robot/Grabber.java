package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Grabber {
    UltRobot robot;
    DcMotor pixelMotor;
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
        pixelMotor = this.robot.linearOpMode.hardwareMap.dcMotor.get("pixelMotor");
    }


    public void update() {
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
