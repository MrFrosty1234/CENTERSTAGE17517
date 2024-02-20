package org.woen.team17517.RobotModules.Lighting;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Lighting implements RobotModule {

    public LightningMode lightMode = LightningMode.ON;
    DcMotor light;
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

    public void smooth() {
        double t = System.currentTimeMillis() / 1000.0;
        setPower((Math.sin(t) + 1) / 2);
    }


    public void update() {
        switch (lightMode) {
            case ON:
                setPower(1);
                break;
            case OFF:
                setPower(0);
                break;
            case SMOOTH:
                smooth();
                break;
        }
    }

    public enum LightningMode {
        OFF, ON, SMOOTH
    }
}

