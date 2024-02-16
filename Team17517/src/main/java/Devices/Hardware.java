package Devices;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Hardware {
    HardwareMap hardwareMap;
    public Hardware(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        intakeAndLiftMotors = new IntakeAndLiftMotors(hardwareMap);
        driveTrainMotors = new DriveTrainMotors(hardwareMap);
        grabberServos = new GrabberServos(hardwareMap);
        odometers = new Odometers(hardwareMap);
        planeServos = new PlaneServos(hardwareMap);
        sensors = new Sensors(hardwareMap);
        lights = new Lights(hardwareMap);
    }
    public IntakeAndLiftMotors intakeAndLiftMotors;
    public DriveTrainMotors    driveTrainMotors;
    public GrabberServos       grabberServos;
    public Odometers           odometers;
    public PlaneServos         planeServos;
    public Sensors             sensors;
    public Lights              lights;

}
