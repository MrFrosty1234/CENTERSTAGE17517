package org.woen.team18742.Tools;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotorEx LeftForwardDrive, LeftBackDrive, RightForwardDrive, RightBackDrive;

    public static DcMotorEx OdometerXLeft, OdometerY, OdometerXRight;

    public static DcMotor LiftMotor, LightingMotor;

    public static DcMotorEx BrushMotor;

    public static WebcamName Camera;

    public static DigitalChannel EndSwitchUp, EndSwitchDown;

    public static IMU IMU;

    public static AnalogInput PixelSensor1, PixelSensor2;
    public static Servo Gripper, Clamp, Servopere, ServoPlane, ServoRailGun;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;

    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftForwardDrive = map.get(DcMotorEx.class, "leftmotor");
        RightForwardDrive = map.get(DcMotorEx.class, "rightmotor");
        LeftBackDrive = map.get(DcMotorEx.class, "leftbackmotor");
        RightBackDrive = map.get(DcMotorEx.class, "rightbackmotor");

        LiftMotor = map.get(DcMotor.class, "liftmotor");

        BrushMotor = map.get(DcMotorEx.class, "brushMotor");

        OdometerXLeft = map.get(DcMotorEx.class, "OdometrXLeft");
        OdometerY = map.get(DcMotorEx.class, "OdometrY");
        OdometerXRight = BrushMotor;

        Camera = map.get(WebcamName.class, "Webcam 1");

        EndSwitchUp = map.get(DigitalChannel.class, "ending1");
        EndSwitchDown = map.get(DigitalChannel.class, "ending2");

        ServoPlane = map.get(Servo.class, "servoPlane");
        ServoRailGun = map.get(Servo.class, "servoRailGun");

        IMU = map.get(IMU.class, "imu");

        PixelSensor1 = map.get(AnalogInput.class, "pixelSensor1");
        PixelSensor2 = map.get(AnalogInput.class, "pixelSensor2");
        Gripper = map.get(Servo.class, "gripok");
        Clamp = map.get(Servo.class, "gripokiu");
        Servopere = map.get(Servo.class, "perevert");

        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        LightingMotor = OdometerXLeft;

        _hardwareDevices = map;
    }
}
