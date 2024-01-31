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

    public static DcMotorEx Podtyagamotor;

    public static DcMotorEx LiftMotor, LightingMotor;

    public static DcMotorEx BrushMotor;

    public static WebcamName Camera;

    public static DigitalChannel EndSwitchUp, EndSwitchDown;

    public static IMU IMU;

    public static AnalogInput PixelSensor;
    public static Servo Gripper, Clamp, Servopere, ServoPlane, ServoRailGun ,podtyaga1 ,podtyaga2;
    public static Servo leftStackBrush, rightStackBrush, stackLift;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;

    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftForwardDrive = map.get(DcMotorEx.class, "leftFrontMotor");
        RightForwardDrive = map.get(DcMotorEx.class, "rightFrontMotor");
        LeftBackDrive = map.get(DcMotorEx.class, "leftBackMotor");
        RightBackDrive = map.get(DcMotorEx.class, "rightBackMotor");

        LiftMotor = map.get(DcMotorEx.class, "liftMotor");

        BrushMotor = map.get(DcMotorEx.class, "odometerXRightBrush");
        OdometerXLeft = map.get(DcMotorEx.class, "odometerXLeft");
        OdometerY = map.get(DcMotorEx.class, "odometrYLED");
        OdometerXRight = map.get(DcMotorEx.class, "odometerXRightBrush");

        Camera = map.get(WebcamName.class, "Webcam 1");

        EndSwitchUp = map.get(DigitalChannel.class, "endSwitchUp");
        EndSwitchDown = map.get(DigitalChannel.class, "endSwitchDown");

        ServoPlane = map.get(Servo.class, "servoPlane");
        ServoRailGun = map.get(Servo.class, "servoRailGun");

        IMU = map.get(IMU.class, "imu");

        PixelSensor = map.get(AnalogInput.class, "pixelSensor");
        Gripper = map.get(Servo.class, "gripper");
        Clamp = map.get(Servo.class, "clamp");
        Servopere = map.get(Servo.class, "turner");

        leftStackBrush = map.get(Servo.class,"leftStackBrush");

        rightStackBrush = map.get(Servo.class,"rightStackBrush");
        stackLift = map.get(Servo.class,"stackLift");

        podtyaga1 = map.get(Servo.class, "leftHook");
        podtyaga2 = map.get(Servo.class, "rightHook");
        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        LightingMotor = map.get(DcMotorEx.class, "odometrYLED");;
        Podtyagamotor = map.get(DcMotorEx.class, "odometerXLeft");

        _hardwareDevices = map;
    }
}
