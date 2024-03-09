package org.woen.team17517.Service;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.UltRobot;

import java.util.HashMap;

@Config
public class TelemetryOutput implements RobotModule {
    UltRobot robot;
    public static boolean lift = false;
    public static boolean driveTrain = false;
    public static boolean teleOp = false;
    public static boolean odometry = false;
    public static boolean velocity = false;
    public static boolean odometryAndCamera = false;
    public static boolean ftcMap = false;
    public static boolean encoders = false;
    public  static  boolean velocityOdometry = false;
    double dlin =40;
    double shir =40;
    public static boolean opticalSensor = false;
    public double [] rectXPoints = new double[2];
    public double [] rectYPoints = new double[2];

    TelemetryPacket packet = new TelemetryPacket();
    public static boolean grabber = false;
    private final Telemetry telemetry;
    public TelemetryOutput(UltRobot robot){
        this.robot = robot;
        robot.linearOpMode.telemetry = new MultipleTelemetry(robot.linearOpMode.telemetry,FtcDashboard.getInstance().getTelemetry());
        telemetry = robot.linearOpMode.telemetry;
    }

    public void update(){
        if(lift) {
            telemetry.addData("cleanPos", robot.lift.getCleanPosition());
            telemetry.addData("lift mode", robot.lift.getLiftMode());
            telemetry.addData("pos", robot.lift.getPosition());
            telemetry.addData("liftTarget", robot.lift.getTargetPosition().get());
            telemetry.addData("isAtPosition", robot.lift.isAtPosition());
            telemetry.addData("buttonDown", robot.lift.getDownSwitch());
            telemetry.addData("buttonUp", robot.lift.getUpSwitch());
            telemetry.addData("Current", robot.lift.getCurent());
            telemetry.addData("speedForPid",robot.lift.getPower());
        }
        if (opticalSensor){
            telemetry.addData("PixelsIn",robot.pixelsCount.isPixels(10000));
        }
        if (grabber){
            telemetry.addData("GrabberProgibTarget",robot.grabber.getProgibTarget());
            telemetry.addData("GrabberTargetOpenClose",robot.grabber.getOpenCloseTarget());
            telemetry.addData("BrushCurrent", robot.hardware.intakeAndLiftMotors.brushMotor.getCurrent(CurrentUnit.AMPS));
        }
        if(driveTrain){
            HashMap<String,Double> targetMap = robot.driveTrain.getTargets();
            HashMap<String,Double> errorMap = robot.driveTrain.getErrors();
            HashMap<String,Double> positionMap = robot.driveTrain.getPosition();
            telemetry.addData("AutoMode",robot.driveTrain.autoMode);
            telemetry.addData("isAtPos",robot.driveTrain.isAtPosition());


            telemetry.addData("error X", errorMap.get("X"));
            telemetry.addData("target X", targetMap.get("X"));
            telemetry.addData("pos X", positionMap.get("X"));

            telemetry.addData("error Y", errorMap.get("Y"));
            telemetry.addData("target Y", targetMap.get("Y"));
            telemetry.addData("pos Y", positionMap.get("Y"));

            telemetry.addData("error H", errorMap.get("H"));
            telemetry.addData("target H", targetMap.get("H"));
            telemetry.addData("pos H", positionMap.get("H"));
        }
        if(teleOp) {
            boolean liftUpAuto            = robot.linearOpMode.gamepad1.triangle;
            boolean liftDownAuto          = robot.linearOpMode.gamepad1.cross;

            boolean brushIn               = robot.linearOpMode.gamepad1.left_trigger > 0.1;
            boolean brushOut              = robot.linearOpMode.gamepad1.left_bumper;

            boolean openAndFinishGrabber  = robot.linearOpMode.gamepad1.circle;
            boolean closeAndSafeGrabber   = robot.linearOpMode.gamepad1.square;

            boolean liftUpMan             = robot.linearOpMode.gamepad1.dpad_up;
            boolean liftDownMan           = robot.linearOpMode.gamepad1.dpad_down;

            boolean startPlane = robot.linearOpMode.gamepad1.ps;
            boolean aimPlane = robot.linearOpMode.gamepad1.right_stick_button;

            telemetry.addData("aimPlane",aimPlane);
            telemetry.addData("startPlane",startPlane);
            telemetry.addData("brushIn",brushIn);
            telemetry.addData("brushOut",brushOut);
            telemetry.addData("openAndFinishGrabber",openAndFinishGrabber);
            telemetry.addData("closeAndFinishGrabber",closeAndSafeGrabber);
            telemetry.addData("liftUoMan",liftUpMan);
            telemetry.addData("liftDownMan",liftDownMan);
            telemetry.addData("liftUpAuto",liftUpAuto);
            telemetry.addData("liftDownAuto",liftDownAuto);
        }
        if(odometry){
            telemetry.addData("x",robot.odometry.getX());
            telemetry.addData("y",robot.odometry.getY());
            telemetry.addData("heading",robot.odometry.getH());
            telemetry.addData("cleanX", robot.odometry.posOdometerLeftY);
            telemetry.addData("cleanY", robot.odometry.posOdometerRightY);
            telemetry.addData("cleanH", robot.odometry.posOdometerX);
        }
        if (velocityOdometry){
            telemetry.addData("xVel",robot.odometry.getVelCleanX());
            telemetry.addData("yVel",robot.odometry.getVelCleanY());
            telemetry.addData("heading",robot.odometry.getVelCleanH());
            telemetry.addData("leftY", robot.odometry.getCleanLeftY());
            telemetry.addData("RightY", robot.odometry.getCleanRightY());
            telemetry.addData("hardSpeedY",robot.odometry.getHardVelOdometerRightY());
            telemetry.addData("hardSpeedX",robot.odometry.getHardVelOdometerX());
            telemetry.addData("hardSpeedH",robot.odometry.getHardVelOdometerLeftY());
            telemetry.addData("mathSpeedX",robot.odometry.getMathSpeedOdometerX());
            telemetry.addData("mathSpeedY",robot.odometry.getMathSpeedOdometerRightY());
            telemetry.addData("mathSpeedH",robot.odometry.getMathSpeedOdometerLeftY());
        }
        if(velocity){
            HashMap<String,Double> encoderMap = robot.driveTrainVelocityControl.getEncoders();
            HashMap<String,Double> powerMap = robot.driveTrainVelocityControl.getPowers();
            HashMap<String,Double> targetMap = robot.driveTrainVelocityControl.getTargets();
            telemetry.addData("TargetY",targetMap.get("targetY"));
            telemetry.addData("TargetX",targetMap.get("targetX"));
            telemetry.addData("TargetH",targetMap.get("targetH"));
            telemetry.addData("SpeedY",encoderMap.get("yEnc"));
            telemetry.addData("SpeedX",encoderMap.get("xEnc"));
            telemetry.addData("SpeedH",encoderMap.get("hEnc"));
            telemetry.addData("powerY",powerMap.get("powerY"));
            telemetry.addData("powerX",powerMap.get("powerX"));
            telemetry.addData("powerH",powerMap.get("powerH"));
            telemetry.addData("targetAngle",robot.driveTrainVelocityControl.getTargetAngle());
            telemetry.addData("posAngle",robot.driveTrainVelocityControl.getAngle());

        }
        telemetry.update();
    }
}
