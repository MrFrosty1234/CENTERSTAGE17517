package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

public class Configs {
    @Config
    public static class Suspension{
        public static double nulevayapodtyaga1 = 1;
        public static double nulevayapodtyaga2 = 1;
        public static double rasstrelennayatyga1 = 0.53;
        public static double rasstrelennayatyga2 = 0.49;
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

        public static double ZoneLeftEndRed = 0, ZoneForwardEndRed = 300;
        public static double ZoneLeftEndBlue = 40, ZoneForwardEndBlue = 310;

        public static int PruningStart = 290;

        public static int ksize = 22;

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
        public static double DOWN_MOVE_POWER = -0.00003, DOWN_AT_TARGET_POWER = 0, DOWN_MOVE_POWER_FAST = -0.02;
    }

    @Config
    public static class LiftPoses{
        public static int POSE_UP = 1100;
        public static int POSE_MIDDLE_UPPER = 490;
        public static int POSE_MIDDLE_LOWER = 490;
        public static int POSE_DOWN = -40;
        public static int POSE_DOWN_ENDSWITCH_THRESHOLD = 10;
        public static double PCoef = 0.1, ICoef = 0, DCoef = 0.1;
    }

    @Config
    public static class Lift{
        public static double isProchelnugnoepologenie = 420;
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
        public static double wheelDiameter = 9.6, encoderconstat = 480d / (26d / 22d), MaxSpeedX = 155, MaxSpeedTurn = Math.toRadians(110), speed = 0.5;
        public static double MaxTurnAccel = Math.toRadians(110); //random
        public static double Radius = 15.7;
    }

    @Config
    public static class Gyroscope{
        public static double MergerCoefSeconds = 0.7;
    }

    @Config
    public static class Intake{
        public static double pixelSensorvoltage = 0.128, PixelCenterOpen = 0.72;//0.4 pixelSensorVoltage = 0.15
        public static double servoTurnNormal = 0.68;
        public static final double servoTurnTurned = 0.06;
        public static double servoGripperNormal = 0.95;
        public static double servoGripperGripped = 0.765;
        public static double servoClampClamped = 0.65;
        public static double servoClampReleased = 0.47;
        public static double servoClampReleasedLift = 0.37;
        public static double pixelDetectTimeMs = 235;
    }

    @Config
    public static class Plane{
        public static double servoplaneOtkrit = 0.17;
        public static double servoplaneneOtkrit = 0.03;
    }

    @Config
    public static class Route{
        public static double MinProfileAccel = -150; // random
        public static double MaxProfileAccel = 150; // random
    }

    @Config
    public static class PositionConnection{
        public static double Axial = 2.49;
        public static double Lateral = 2.49;
        public static double Heading = 5.3;
    }

    @Config
    public static class SpeedConnection{
        public static double Axial = 0.21;
        public static double Lateral = 0.21;
        public static double Heading = 0.05;
    }

    @Config
    public static class Motors{
        public static double DefultP = 0.000001;
        public static double DefultI = 0;//0.00013;
        public static double DefultD = 0.005;
        public static double DefultF = 0.0005;
    }

    @Config
    public static class Battery{
        public static double CorrectCharge = 14;
    }

    @Config
    public static class StackBrush{
        public static double LEFT_SERVO_STOP = 0.435;
        public static double LEFT_SERVO_FWD = 0.0;
        public static double LEFT_SERVO_REV = 1.0;
        public static double RIGHT_SERVO_STOP = 0.421;
        public static double RIGHT_SERVO_FWD = 1.0;
        public static double RIGHT_SERVO_REV = 0.0;
        public static double SERVO_LIFT_DOWN = 0.617;
        public static double SERVO_LIFT_UP = 0.850;
    }
}
