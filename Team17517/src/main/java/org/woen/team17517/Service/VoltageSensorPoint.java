package org.woen.team17517.Service;


import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class VoltageSensorPoint implements RobotModule
{
    UltRobot robot;
    private VoltageSensor voltageSensor;
    private static double voltage = 0;

    public VoltageSensorPoint(UltRobot robot)
    {
        this.robot = robot;
        voltageSensor = robot.linearOpMode.hardwareMap.voltageSensor.get("Control Hub");
    }


    public void update() {
        voltage = voltageSensor.getVoltage();
    }
    public double getVol(){return voltage;}
}
