package org.woen.team17517.RobotModules;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.Service.RobotModule;

public class Lighting implements RobotModule {

    public LightningMode lightMode = LightningMode.OFF;
    DcMotor svet1;
    DcMotor svet2;
    UltRobot robot;
    public Lighting(UltRobot robot) {
        this.robot = robot;
       // svet1 = this.robot.linearOpMode.hardwareMap.dcMotor.get("svet1");
       // svet2 = this.robot.linearOpMode.hardwareMap.dcMotor.get("svet2");
      //  svet1.setDirection(DcMotorSimple.Direction.FORWARD);
       // svet2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPower(double x) {
       // svet1.setPower(x);
        //svet2.setPower(x);
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

