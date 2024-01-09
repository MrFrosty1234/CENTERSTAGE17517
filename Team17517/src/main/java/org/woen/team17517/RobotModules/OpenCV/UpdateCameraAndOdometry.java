package org.woen.team17517.RobotModules.OpenCV;

import static java.lang.Math.sqrt;

import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.RobotModules.UltRobot;

public class UpdateCameraAndOdometry implements RobotModule {

    UltRobot robot;

    public boolean preority = true;
    public static double cameraToOdometrySafeEncs = 50;
    double[] coords = new double[3];

    public UpdateCameraAndOdometry(UltRobot robot) {
        this.robot = robot;
    }

    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update() {
        robot.testAprilTagPipeline.startPipeline();
        robot.testAprilTagPipeline.visionPortalWork();
        double xVector = robot.testAprilTagPipeline.rawTagPoseVector.get(0);
        double yVector = robot.testAprilTagPipeline.rawTagPoseVector.get(1);

        if (sqrt(xVector * xVector + yVector * yVector) < cameraToOdometrySafeEncs) {
            preority = false;
            coords[0] = xVector;
            coords[1] = yVector;
            robot.odometry.x = coords[0];
            robot.odometry.y = coords[1];
        } else {
            coords[0] = robot.odometry.x;
            coords[1] = robot.odometry.y;
            preority = true;
        }
    }
}