package org.woen.team17517.Robot;


import com.qualcomm.robotcore.hardware.VoltageSensor;

public class VoltageSensorPoint
{
    UltRobot robot;
    private VoltageSensor voltageSensor;
    private static double voltage = 0;
    public VoltageSensorPoint(UltRobot robot)
    {
        this.robot = robot;
        voltageSensor = robot.linearOpMode.hardwareMap.get(VoltageSensor.class,"voltageSensor");
    }
    public void update() {
        voltage = voltageSensor.getVoltage();
    }
    public double getVol(){return voltage;}
}
