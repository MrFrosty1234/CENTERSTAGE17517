package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

public class Configs {
    @Config
    public static class Suspension{
        public static double nulevayapodtyaga1 = 0.43;
        public static double nulevayapodtyaga2 = 0.43;
        public static double rasstrelennayatyga1 = 0.23;
        public static double rasstrelennayatyga2 = 0.23;
    }



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
        public static double CameraX = 16.01, CameraY = -16.18;

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
        public static double PCoef = 0.01, ICoef = 0, DCoef = 0, GCoef = 0.1;
        public static double DOWN_MOVE_POWER = 0, DOWN_AT_TARGET_POWER = 0;
    }

    @Config
    public static class LiftPoses{
        public static int POSE_UP = 900;
        public static int POSE_MIDDLE_UPPER = 900;
        public static int POSE_MIDDLE_LOWER = 900;
        public static int POSE_SERVO_CLEARANCE = 900;
        public static int POSE_DOWN = -40;
        public static int POSE_DOWN_ENDSWITCH_THRESHOLD = 10;
        public static double PCoef = 0.1, ICoef = 0, DCoef = 0.1;
    }

    @Config
    public static class Odometry{
        public static double YCoef = 0.5;
        public static double XCoef = 0.5;

        public static double RadiusOdometrXLeft = 15.117, RadiusOdometrXRight = 15.315, RadiusOdometrY = 16.8609;

        public static double DiametrOdometr = 4.8, EncoderconstatOdometr = 8192;

        public static double YLag = 0.7;
        public static double RotateLag = 0.89;
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
        public static double protectionCurrentAmps = 3.3;
        public static double protectionTimeThresholdMs = 700;
        public static double reverseTimeThresholdMs = 900;
        public static double brushPower = 1.0;
        public static double brushPowerReverse = -1.0;
    }

    @Config
    public static class DriveTrainWheels {
        public static double wheelDiameter = 9.6, encoderconstat = 480d / (26d / 22d), MaxSpeedX = 150, MaxSpeedTurn = Math.toRadians(130), speed = 0.5;
        public static double MaxTurnAccel = 3; //random
        public static double Radius = 15.7;
    }

    @Config
    public static class Gyroscope{
        public static double MergerCoefSeconds = 0.7;
    }

    @Config
    public static class Intake{
        public static double pixelSensorvoltage = 0.137, PixelCenterOpen = 0;//0.4 pixelSensorVoltage = 0.15
        public static double servoTurnNormal = 0.68;
        public static final double servoTurnTurned = 0.06 ;
        public static long AverageTime = 830;
        public static double servoGripperNormal = 0.95;
        public static double servoGripperGripped = 0.765;
        public static double servoClampClamped = 0.65;
        public static double servoClampReleased = 0.41 ;//0.38? //TODO THIS BREAKS THE ROBOT WHEN GOING THROUGH TRUSS
        public static double pixelDetectTimeMs = 200;
    }

    @Config
    public static class Plane{
        public static double servoplaneOtkrit = 0.07;
        public static double servoplaneneOtkrit = 0.17;
    }

    @Config
    public static class Route{
        public static double MinProfileAccel = -100; // random
        public static double MaxProfileAccel = 100; // random
    }

    @Config
    public static class PositionConnection{
        public static double Axial = 0.0000012;
        public static double Lateral = 0.0000012;
        public static double Heading = 0.4;
    }

    @Config
    public static class SpeedConnection{
        public static double Axial = 0.15;
        public static double Lateral = 0.15;
        public static double Heading = 0.15;
    }

    @Config
    public static class Motors{
        public static double DefultP = 0.000001;
        public static double DefultI = 0.00013;
        public static double DefultD = 0.005;
        public static double DefultF = 0.0004;
    }

    @Config
    public static class Battery{
        public static double CorrectCharge = 14;
    }

    @Config
    public static class StackBrush{
        public static double LEFT_SERVO_STOP = 0.464;
        public static double LEFT_SERVO_FWD = 0.0;
        public static double LEFT_SERVO_REV = 1.0;
        public static double RIGHT_SERVO_STOP = 0.418;
        public static double RIGHT_SERVO_FWD = 1.0;
        public static double RIGHT_SERVO_REV = 0.0;
        public static double SERVO_LIFT_DOWN = 0.617;
        public static double SERVO_LIFT_UP = 0.875;
    }
}
