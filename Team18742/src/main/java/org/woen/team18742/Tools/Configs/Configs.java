package org.woen.team18742.Tools.Configs;

import android.content.Context;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.ftccommon.external.OnCreate;

import java.lang.reflect.Field;

public class Configs {
    @Config
    public static class GeneralSettings {
        public static boolean IsAutonomEnable = true;

        public static boolean IsUseOdometers = false;

        public static boolean IsCachinger = true;

        public static boolean IsCameraDebug = false;

        public static boolean TelemetryOn = true;
    }

    @Config
    public static class Camera{
        public static int RobotPos = 2;
        public static double CameraAccuracy = 130;
        public static double CameraX = 16.01, CameraY = 16.18;
    }

    @Config
    public static class LiftPid{
        public static double PCoef = 0.1, ICoef = 0, DCoef = 0.1;
    }

    @Config
    public static class Odometry{
        public static double YCoef = 0.9;
        public static double XCoef = 0.9;

        public static double RadiusOdometrXLeft = 15.117, RadiusOdometrXRight = 15.315, RadiusOdometrY = 16.8609;

        public static double DiametrOdometr = 4.8, EncoderconstatOdometr = 8192;
    }

    @Config
    public static class AutomaticForwardPid{
        public static double PidForwardP = 0.1, PidForwardI = 0, PidForwardD = 1;
    }

    @Config
    public static class AutomaticSidePid{
        public static double PidSideP = 0.2, PidSideI = 0, PidSideD = 1;
    }

    @Config
    public static class AutomaticRotatePid{
        public static double PidRotateP = 1, PidRotateI = 0, PidRotateD = 1;
    }

    @Config
    public static class Brush{
        public static double protectionCurrentAmps = 1;
        public static double protectionTimeThreshold = 1500;
        public static double reverseTimeThreshold = 3000;
    }

    @Config
    public static class DriverTrainWheels{
        public static double diameter = 9.8, encoderconstat = 1440;
    }

    @Config
    public static class Gyroscope{
        public static double MergerCoefSeconds = 0.9;
    }

    @Config
    public static class Intake{
        public static double pixelSensorvoltage = 0.125, PixelCenterOpen = 0;//0.4
        public static double servoTurnNormal = 0.96;
        public static final double servoTurnTurned = 0.35;
        public static long AverageTime = 830;
        public static double servoGripperNormal = 0.4;
        public static double servoGripperGripped = 0.13;
        public static double servoClampClamped = 0.9;
        public static double servoClampReleased = 0.42;//0.5
        public static double pixelDetectTimeMs = 1000;
        public static double ReverseTimeMs = 2000;
    }

    @Config
    public static class Plane{
        public static double servoplaneOtkrit = 0.07;
        public static double servoplaneneOtkrit = 0.17;
    }

    @Config
    public static class DriverTrainLeftBackPidf{
        public static double pCof = 1;
        public static double iCof = 0;
        public static double dCof = 0;
        public static double fCof = 0;
    }

    @Config
    public static class DriverTrainRightBackPidf{
        public static double pCof = 1;
        public static double iCof = 0;
        public static double dCof = 0;
        public static double fCof = 0;
    }

    @Config
    public static class DriverTrainRightForwardPidf{
        public static double pCof = 1;
        public static double iCof = 0;
        public static double dCof = 0;
        public static double fCof = 0;
    }

    @Config
    public static class DriverTrainLeftForwardPidf{
        public static double pCof = 1;
        public static double iCof = 0;
        public static double dCof = 0;
        public static double fCof = 0;
    }
}
