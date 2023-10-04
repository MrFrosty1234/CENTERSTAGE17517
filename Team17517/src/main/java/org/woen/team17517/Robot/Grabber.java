package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Grabber {
    UltRobot robot;
    DcMotor pixelMotor;
    DigitalChannel pixelSensorRight;
    DigitalChannel pixelSensorLeft;
    double pixelsCount = 0;
    double pixelsCountOld = 0;
    double targetPower = 1;

    public Grabber(UltRobot robot) {
        this.robot = robot;
        pixelSensorLeft = this.robot.linearOpMode.hardwareMap.digitalChannel.get("pixelStorageLeft");
        pixelSensorRight = this.robot.linearOpMode.hardwareMap.digitalChannel.get("pixelStorageRight");
        pixelMotor = this.robot.linearOpMode.hardwareMap.dcMotor.get("pixelMotor");
    }


    public void update() {
        pixelsCountOld = pixelsCount;
            if (pixelSensorLeft.getState() && pixelSensorRight.getState()) {
                pixelMotor.setPower(targetPower);
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
