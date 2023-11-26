package org.woen.team17517.Robot;

import static java.lang.Math.sqrt;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class updateCameraAndOdometry  implements RobotModule{

    UltRobot robot;

    public boolean preority = true;
    public static double cameraToOdometrySafeEncs = 50;
    double[] coords = new double[3];

    public updateCameraAndOdometry(UltRobot robot) {
        this.robot = robot;
    }

    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update() {
        robot.testAprilTagPipeline.visionPortalWork();
        double xVector = robot.testAprilTagPipeline.rawTagPoseVector.get(0);
        double yVector = robot.testAprilTagPipeline.rawTagPoseVector.get(1);
        double zVector = robot.testAprilTagPipeline.rawTagPoseVector.get(2);

        if (sqrt(xVector * xVector + yVector * yVector + zVector * zVector) < cameraToOdometrySafeEncs) {
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