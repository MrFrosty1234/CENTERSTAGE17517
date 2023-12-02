package org.woen.team17517.Robot;


import com.qualcomm.robotcore.hardware.VoltageSensor;

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

    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update() {
        voltage = voltageSensor.getVoltage();
    }
    public double getVol(){return voltage;}
}
