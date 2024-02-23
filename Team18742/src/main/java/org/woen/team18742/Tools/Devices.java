package org.woen.team18742.Tools;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.woen.team18742.Tools.Motor.EncoderOnly;
import org.woen.team18742.Tools.Motor.MotorOnly;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotorEx LeftForwardDrive, LeftBackDrive, RightForwardDrive, RightBackDrive;

    public static DcMotorEx OdometerXLeft, OdometerY, OdometerXRight;

    public static DcMotorEx HookMotor;

    public static DcMotorEx LiftMotor, LightingMotor;

    public static DcMotorEx BrushMotor;

    public static WebcamName Camera;

    public static DigitalChannel EndSwitchUp, EndSwitchDown;

    public static IMU IMU;

    public static AnalogInput PixelSensor;
    public static Servo Gripper, Clamp, Servopere, ServoPlane, HookServoLeft, HookServoRight, stackLift, LineServo;
    public static ServoImplEx leftStackBrush, rightStackBrush;

    public static List<LynxModule> Hubs;
    public static VoltageSensor VoltageSensor;
    public static List<ServoImplEx> Servs;

    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftForwardDrive = map.get(DcMotorEx.class, "leftFrontMotor");
        RightForwardDrive = map.get(DcMotorEx.class, "rightFrontMotor");
        LeftBackDrive = map.get(DcMotorEx.class, "leftBackMotor");
        RightBackDrive = map.get(DcMotorEx.class, "rightBackMotor");

        Servs = map.getAll(ServoImplEx.class);

        LiftMotor = map.get(DcMotorEx.class, "liftMotor");

        BrushMotor = map.get(MotorOnly.class, "odometerXRightBrush");
        OdometerXLeft = map.get(EncoderOnly.class, "odometerXLeft");
        OdometerY = map.get(EncoderOnly.class, "odometrYLED");
        OdometerXRight = map.get(EncoderOnly.class, "odometerXRightBrush");

        Camera = map.get(WebcamName.class, "Webcam 1");

        EndSwitchUp = map.get(DigitalChannel.class, "endSwitchUp");
        EndSwitchDown = map.get(DigitalChannel.class, "endSwitchDown");

        ServoPlane = map.get(Servo.class, "servoPlane");

        IMU = map.get(IMU.class, "imu");

        PixelSensor = map.get(AnalogInput.class, "pixelSensor");
        Gripper = map.get(Servo.class, "gripper");
        Clamp = map.get(Servo.class, "clamp");
        Servopere = map.get(Servo.class, "turner");

        leftStackBrush = map.get(ServoImplEx.class,"leftStackBrush");
        rightStackBrush = map.get(ServoImplEx.class,"rightStackBrush");

        stackLift = map.get(Servo.class,"stackLift");

        HookServoLeft = map.get(Servo.class, "leftHook");
        HookServoRight = map.get(Servo.class, "rightHook");
        Hubs = map.getAll(LynxModule.class);

        VoltageSensor = map.get(VoltageSensor.class, "Control Hub");

        LightingMotor = map.get(MotorOnly.class, "odometrYLED");;
        HookMotor = map.get(MotorOnly.class, "odometerXLeft");

        LineServo = map.get(Servo.class, "lineServo");

        _hardwareDevices = map;
    }
}
