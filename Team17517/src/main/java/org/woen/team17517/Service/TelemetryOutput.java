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
    public static boolean grabber = false;
    public static boolean odometry = false;
    public static boolean velocity = false;
    public static boolean odometryAndCamera = false;
    public static boolean ftcMap = false;
    public static boolean encoders = false;
    public  static  boolean cleanOdometry = false;
    double dlin =40;
    double shir =40;
    public double [] rectXPoints = new double[2];
    public double [] rectYPoints = new double[2];

    TelemetryPacket packet = new TelemetryPacket();
    
    private final Telemetry telemetry;
    public TelemetryOutput(UltRobot robot){
        this.robot = robot;
        robot.linearOpMode.telemetry = new MultipleTelemetry(robot.linearOpMode.telemetry,FtcDashboard.getInstance().getTelemetry());
        telemetry = robot.linearOpMode.telemetry;
    }

    public void update(){
        if(lift) {
            telemetry.addData("liftEncs", robot.lift.liftMotor.getCurrentPosition());
            telemetry.addData("target",robot.lift.getTargetPosition().value);
            telemetry.addData("lift pos",robot.lift.getTargetPosition());
            telemetry.addData("lift mode", robot.lift.liftMode);
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
        if(grabber) {
            telemetry.addData("pixels count",robot.grabber.pixelsCount);
            telemetry.addData("pixelSensorLeft",robot.grabber.pixelSensorLeft);
            telemetry.addData("pixelSensorRight",robot.grabber.pixelSensorRight);
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
