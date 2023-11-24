package org.woen.team17517.Robot;

import static java.lang.Math.sqrt;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

public class updateCameraAndOdometry {

    UltRobot robot;

    public boolean preority = true;

    public updateCameraAndOdometry(UltRobot robot) {
        this.robot = robot;
    }
    public void update() {
        robot.testAprilTagPipeline.visionPortalWork();
        double x = robot.testAprilTagPipeline.x;
        double y = robot.testAprilTagPipeline.y;
        double z = robot.testAprilTagPipeline.z;

        if ((sqrt(x*x + y*y + z*z) - robot.testAprilTagPipeline.rawTagPoseVector) < 50) {
            preority = false;
        } else {
            preority = true;
        }
        robot.odometry.update();
    }
}
