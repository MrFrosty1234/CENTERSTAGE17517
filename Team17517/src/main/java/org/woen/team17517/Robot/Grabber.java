package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Grabber {
    public static double POS_CLOSE = 0.75;
    public static double POS_OPEN = 1.00;
    Servo servo;
    Servo servo1;
    UltRobot robot;
    boolean targetPosition = false;
    double timeGrabber;

    public Grabber(UltRobot robot) {
        this.robot = robot;
        servo = this.robot.linearOpMode.hardwareMap.get(Servo.class, "Servo1");
        servo1 = this.robot.linearOpMode.hardwareMap.get(Servo.class, "Servo");
    }

    public void setPosition(boolean closeGrabber) {
        if (targetPosition != closeGrabber)
            timeGrabber = System.currentTimeMillis();
        if (closeGrabber)
            servo.setPosition(POS_CLOSE);
        else
            servo.setPosition(POS_OPEN);
        targetPosition = closeGrabber;
    }


    public boolean isClosed() {
        if (System.currentTimeMillis() < timeGrabber + 300)
            return !targetPosition;
        else
            return targetPosition;
    }

}