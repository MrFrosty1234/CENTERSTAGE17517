package org.woen.team18742.Tools;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.List;

public class Devices {
    private static HardwareMap _hardwareDevices;

    public static DcMotor LeftForwardDrive, LeftBackDrive, RightForwardDrive, RightBackDrive;

    public static DcMotor OdometrXLeft, OdometrY, OdometrXRight;

    public static DcMotor LiftMotor, LightingMotor;

    public static DcMotorEx BrushMotor;

    public static WebcamName Camera;

    public static DigitalChannel Ending1, Ending2;

    public static IMU IMU;

    public static AnalogInput PixelSensor1, PixelSensor2;
    public static Servo Gripper, Clamp, Servopere, ServoPlane, ServoRailGun;

    public static List<LynxModule> Hubs;

    public static void Init(HardwareMap map){
        if(_hardwareDevices != null)
            return;

        LeftForwardDrive = map.get(DcMotor.class, "leftmotor");
        RightForwardDrive = map.get(DcMotor.class, "rightmotor");
        LeftBackDrive = map.get(DcMotor.class, "leftbackmotor");
        RightBackDrive = map.get(DcMotor.class, "rightbackmotor");

        LiftMotor = map.get(DcMotor.class, "liftmotor");

        BrushMotor = map.get(DcMotorEx.class, "brushMotor");

        OdometrXLeft = map.get(DcMotor.class, "OdometrXLeft");
        OdometrY = map.get(DcMotor.class, "OdometrY");
        OdometrXRight = BrushMotor;

        Camera = map.get(WebcamName.class, "Webcam 1");

        Ending1 = map.get(DigitalChannel.class, "ending1");
        Ending2 = map.get(DigitalChannel.class, "ending2");

        ServoPlane = map.get(Servo.class, "servoPlane");
        ServoRailGun = map.get(Servo.class, "servoRailGun");

        IMU = map.get(IMU.class, "imu");

        PixelSensor1 = map.get(AnalogInput.class, "pixelSensor1");
        PixelSensor2 = map.get(AnalogInput.class, "pixelSensor2");
        Gripper = map.get(Servo.class, "gripok");
        Clamp = map.get(Servo.class, "gripokiu");
        Servopere = map.get(Servo.class, "perevert");

        Hubs = map.getAll(LynxModule.class);

        LiftMotor = OdometrXLeft;

        _hardwareDevices = map;
    }
}
