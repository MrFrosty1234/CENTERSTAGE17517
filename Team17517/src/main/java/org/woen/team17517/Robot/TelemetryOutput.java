package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import java.util.HashMap;

@Config
public class TelemetryOutput implements RobotModule{
    UltRobot robot;
    public static boolean lift = false;
    public static boolean driveTrain = false;
    public static boolean grabber = false;
    public static boolean odometry = false;
    public static boolean velocity = false;
    public static boolean odometryAndCamera = false;
    public static boolean ftcMap = true;
    double dlin =40;
    double shir =40;


    public double [] rectXPoints = new double[2];
    public double [] rectYPoints = new double[2];

    TelemetryPacket packet = new TelemetryPacket();
    public TelemetryOutput(UltRobot robot){
        this.robot = robot;
        robot.linearOpMode.telemetry = new MultipleTelemetry(robot.linearOpMode.telemetry,FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update(){

        if(lift) {
            robot.linearOpMode.telemetry.addData("liftEncs", robot.lift.liftMotor.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("target",robot.lift.liftPosition.value);
            robot.linearOpMode.telemetry.addData("lift pos",robot.lift.liftPosition);
            robot.linearOpMode.telemetry.addData("lift mode", robot.lift.liftMode);
        }
        if(driveTrain){
            robot.linearOpMode.telemetry.addData("error X", robot.driveTrain.xError);
            robot.linearOpMode.telemetry.addData("error Y", robot.driveTrain.yError);
            robot.linearOpMode.telemetry.addData("error Heading", robot.driveTrain.headingError);
            robot.linearOpMode.telemetry.addData("encoder left back",robot.driveTrain.left_back_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder right back",robot.driveTrain.right_back_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder left front", robot.driveTrain.left_front_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder right frony", robot.driveTrain.right_front_drive.getCurrentPosition());
        }
        if(grabber) {
            robot.linearOpMode.telemetry.addData("pixels count",robot.grabber.pixelsCount);
    //        robot.linearOpMode.telemetry.addData("pixelSensorLeft",robot.grabber.pixelSensorLeft);
      //      robot.linearOpMode.telemetry.addData("pixelSensorRight",robot.grabber.pixelSensorRight);
        }
        if(odometry){
            robot.linearOpMode.telemetry.addData("x",robot.odometry.x);
            robot.linearOpMode.telemetry.addData("y",robot.odometry.y);
            robot.linearOpMode.telemetry.addData("heading",robot.odometry.heading);
        }
        if(velocity){
            HashMap<String,Double> encoderMap = robot.driveTrainVelocityControl.getEncoders();
            HashMap<String,Double> powerMap = robot.driveTrainVelocityControl.getPowers();
            HashMap<String,Double> targetMap = robot.driveTrainVelocityControl.getTargets();
            robot.linearOpMode.telemetry.addData("TargetY",targetMap.get("targetY"))
                                        .addData("TargetX",targetMap.get("targetX"))
                                        .addData("TargetH",targetMap.get("targetH"));
            robot.linearOpMode.telemetry.addData("SpeedY",encoderMap.get("yEnc"))
                                        .addData("SpeedX",encoderMap.get("xEnc"))
                                        .addData("SpeedH",encoderMap.get("hEnc"));
            robot.linearOpMode.telemetry.addData("powerY",powerMap.get("powerY"))
                                        .addData("powerX",powerMap.get("powerX"))
                                        .addData("powerH",powerMap.get("powerH"));
        }
        if(odometryAndCamera){
            robot.linearOpMode.telemetry.addLine("Camera position:")
                    .addData("x", robot.testAprilTagPipeline.fieldCameraPos.get(0))
                    .addData("y", robot.testAprilTagPipeline.fieldCameraPos.get(1))
                    .addData("z", robot.testAprilTagPipeline.fieldCameraPos.get(2));
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
         robot.linearOpMode.telemetry.update();
    }
}
