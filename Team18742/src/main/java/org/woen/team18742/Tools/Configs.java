package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
    @Config
    public static class GeneralSettings {
        public static boolean IsAutonomEnable = true;

        public static boolean IsUseOdometrs = false;

        public static boolean IsCachinger = true;

        public static boolean IsCameraDebug = false;
    }

    @Config
    public static class Camera{
        public static int RobotPos = 2;

        public static float CameraAccuracy = 130;
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

        public static double DiametrOdometr = 4.8, EncoderconstatOdometr = 1440;
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
        public static double getvolteges = 1;
        public static double timesxz = 1500;
        public static double times1 = 3000;
    }

    @Config
    public static class DriverTrainWheels{
        public static double diametr = 9.8, encoderconstat = 1440;
    }

    @Config
    public static class Gyroscope{
        public static double MergerCoef = 0.9;
    }

    @Config
    public static class Intake{
        public static double pixelSensorvoltage = 0.125, PixelCenterOpen = 0;//0.4
        public static double servoperevorotnazad = 0.96;
        public static final double servoperevorot = 0.35;
        public static long AverageTime = 830;
        public static double servoGripperreturn = 0.4;
        public static double servoGripper = 0.13;
        public static double servoClamp = 0.9;
        public static double servoClampreturn = 0.42;//0.5
        public static long ReversTime = 2000;
    }

    @Config
    public static class Plane{
        public static double servoplaneOtkrit = 0.07;
        public static double servoplaneneOtkrit = 0.17;
    }
}
