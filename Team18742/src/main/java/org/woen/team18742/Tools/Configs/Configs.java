package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.config.Config;

public class Configs {
    @Config
    public static class GeneralSettings {
        public static boolean IsAutonomEnable = true;

        public static boolean IsUseOdometers = true;

        public static boolean IsCachinger = true;

        public static boolean IsCameraDebug = false;

        public static boolean TelemetryOn = true;
    }

    @Config
    public static class Camera{
        public static int RobotPos = 2;
        public static double CameraAccuracy = 150;
        public static double CameraX = 16.01, CameraY = 16.18;

        public static double ZoneLeftEnd = 220, ZoneForwardEnd = 550;

        public static int PruningStart = 290;

        public static int ksize = 10;

        public static double hRedDown = 4;
        public static double cRedDown = 127.7;
        public static double vRedDowm = 154.4;
        public static double hRedUp = 30;
        public static double cRedUp = 255;
        public static double vRedUp = 255;

        public static double hBlueDown = 95;
        public static double cBlueDown = 170;
        public static double vBlueDowm = 0;
        public static double hBlueUp = 255;
        public static double cBlueUp = 255;
        public static double vBlueUp = 255;
    }

    @Config
    public static class LiftPid{
        public static double PCoef = 0.1, ICoef = 0, DCoef = 0.1;
    }

    @Config
    public static class Odometry{
        public static double YCoef = 0.5;
        public static double XCoef = 0.5;

        public static double RadiusOdometrXLeft = 15.117, RadiusOdometrXRight = 15.315, RadiusOdometrY = 16.8609;

        public static double DiametrOdometr = 4.8, EncoderconstatOdometr = 8192;
        public static double LateralMultiplier = 0.8;

        public static double YLag = 0.8;
    }

    @Config
    public static class AutomaticForwardPid{
        public static double PidForwardP = 0.03, PidForwardI = 0, PidForwardD = 0;
    }

    @Config
    public static class AutomaticSidePid{
        public static double PidSideP = 0.03, PidSideI = 0, PidSideD = 0;
    }

    @Config
    public static class AutomaticRotatePid{
        public static double PidRotateP = 2, PidRotateI = 0, PidRotateD = 0.5;
    }

    @Config
    public static class Brush{
        public static double protectionCurrentAmps = 1;
        public static double protectionTimeThreshold = 1500;
        public static double reverseTimeThreshold = 3000;
    }

    @Config
    public static class DriveTrainWheels {
        public static double diameter = 9.8, encoderconstat = 480 / 1.1, MaxSpeedX = 2052, MaxSpeedY = 2052, MaxSpeedTurn = 1026, speed = 0.5;
        public static boolean isUsePids = false;
        public static double MaxTurnVelocity = 1; //random
    }

    @Config
    public static class Gyroscope{
        public static double MergerCoefSeconds = 0.7;
    }

    @Config
    public static class Intake{
        public static double pixelSensorvoltage = 0.15, PixelCenterOpen = 0;//0.4
        public static double servoTurnNormal = 0.96;
        public static final double servoTurnTurned = 0.35;
        public static long AverageTime = 830;
        public static double servoGripperNormal = 0.4;
        public static double servoGripperGripped = 0.122;
        public static double servoClampClamped = 0.9;
        public static double servoClampReleased = 0.47 ;//0.5
        public static double pixelDetectTimeMs = 1000;
        public static double ReverseTimeMs = 2000;
    }

    @Config
    public static class Plane{
        public static double servoplaneOtkrit = 0.07;
        public static double servoplaneneOtkrit = 0.17;
    }

    @Config
    public static class Route{
        public static double TrackWidth = 1; // random
        public static double MinProfileAccel = -1; // random
        public static double MaxProfileAccel = 1; // random
    }

    @Config
    public static class PositionConnection{
        public static double Axial = 0.01; // random
        public static double Lateral = 0.01; // random
        public static double Heading = 0.01; // random
    }

    @Config
    public static class SpeedConnection{
        public static double Axial = 0.01; // random
        public static double Lateral = 0.01; // random
        public static double Heading = 0.01; // random
    }

    @Config
    public static class Motors{
        public static double DefultP = 0.000001;
        public static double DefultI = 0.00013;
        public static double DefultD = 0.005;
        public static double DefultF = 0.004;
    }

    @Config
    public static class Battery{
        public static double CorrectCharge = 14;
    }
}
