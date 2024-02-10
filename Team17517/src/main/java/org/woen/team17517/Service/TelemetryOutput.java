package org.woen.team17517.Service;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    public  static  boolean cleanOdometry = false;
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
            telemetry.addData("posEncoder", robot.lift.getEncoderPosition());
            telemetry.addData("liftTarget", robot.lift.getTargetPosition());
            telemetry.addData("isAtPosition", robot.lift.isAtPosition());
            telemetry.addData("buttonDown", robot.lift.getDownSwitch());
            telemetry.addData("buttonUp", robot.lift.getUpSwitch());
        }
        if (opticalSensor){
            telemetry.addData("PixelsIn",robot.pixelsCount.isTwoPixelsCount());
            telemetry.addData("Up",robot.pixelsCount.getUpVolt());
            telemetry.addData("Down",robot.pixelsCount.getDownVolt());
        }
        if (grabber){
            telemetry.addData("GrabberProgibTarget",robot.grabber.getTargetProgib());
            telemetry.addData("GrabberTargetOpenClose",robot.grabber.getTargetOpenClose());
        }
        if(driveTrain){
            HashMap<String,Double> targetMap = robot.drivetrainNew.getTargets();
            HashMap<String,Double> errorMap = robot.drivetrainNew.getErrors();
            HashMap<String,Double> positionMap = robot.drivetrainNew.getPosition();
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
            telemetry.addData("x",robot.odometryNew.getX());
            telemetry.addData("y",robot.odometryNew.getY());
            telemetry.addData("heading",robot.odometryNew.getH());
        }
        if (cleanOdometry){
            telemetry.addData("x",robot.odometryNew.getVelCleanX());
            telemetry.addData("y",robot.odometryNew.getVelCleanY());
            telemetry.addData("heading",robot.odometryNew.getVelCleanH());
            telemetry.addData("leftY", robot.odometryNew.getCleanLeftY());
            telemetry.addData("RightY", robot.odometryNew.getCleanRightY());
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
        }
        if(odometryAndCamera){
            telemetry.addLine("Camera position:");
            telemetry.addData("x", robot.testAprilTagPipeline.fieldCameraPos.get(0));
            telemetry.addData("y", robot.testAprilTagPipeline.fieldCameraPos.get(1));
            telemetry.addData("z", robot.testAprilTagPipeline.fieldCameraPos.get(2));
        }
        if(encoders){
            telemetry.addLine("Encoders:");
            telemetry.addData("lbd", robot.odometry.left_back_drive.getCurrentPosition());
            telemetry.addData("lfd", robot.odometry.left_front_drive.getCurrentPosition());
            telemetry.addData("rbd", robot.odometry.right_back_drive.getCurrentPosition());
            telemetry.addData("rfd", robot.odometry.right_front_drive.getCurrentPosition());
            telemetry.addData("h", robot.odometry.heading);
        }
        if(ftcMap){
            packet = new TelemetryPacket();
            double dlin =40;
            double shir =40;

            /*
            packet.fieldOverlay().fillPolygon()
            Vector2D vector2D = new Vector2D(robot.odometry.x,robot.odometry.y);
            Vector2D vectordlin = new Vector2D(dlin/2, shir/2);
            Vector2D rotatedVector = vector2D.vectorRat(robot.odometry.heading);
            double vectorvivad1 = rotatedVector + vectordlin;
            Vector2D vectorvivad = vector2D.vectorRat( vectorvivad1);
            */

            rectXPoints [0] = robot.odometry.x + 20;
            rectXPoints [1] = robot.odometry.x - 20;
            rectYPoints [0] = robot.odometry.y + 20;
            rectYPoints [1] = robot.odometry.y - 20;
            packet.fieldOverlay().fillPolygon(rectXPoints, rectYPoints);
        }
        telemetry.update();
    }
}
