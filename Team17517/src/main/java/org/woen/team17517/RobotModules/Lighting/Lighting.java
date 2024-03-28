package org.woen.team17517.RobotModules.Lighting;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Lighting implements RobotModule {

    public LightningMode lightMode = LightningMode.OFF;
    DcMotorEx light;
    DcMotorEx light2;
    UltRobot robot;
    public Lighting(UltRobot robot) {
        this.robot = robot;
        light = robot.hardware.lights.light1;
        light2 = robot.hardware.lights.light2;
    }

    public void setPower(double x) {
        light2.setPower(-x);
        light.setPower(-x);
    }

    public void on() {
        lightMode = LightningMode.ON;
    }


    public void update() {
        switch (lightMode) {
            case ON:
                setPower(1);
                break;
            case OFF:
                setPower(0);
                break;
        }
    }

    public enum LightningMode {
        OFF, ON
    }
}

