package org.woen.team17517.Robot;

public class updateCameraAndOdometry {

    UltRobot robot;

    public boolean preority = true;

    public updateCameraAndOdometry(UltRobot robot) {
        this.robot = robot;
    }
    public void update(){
        robot.testAprilTagPipeline.visionPortalWork();

        if(robot.testAprilTagPipeline.fieldCameraPos.get(0) < 50){
            preority = false;
        }
        else {
            preority = true;
        }
        robot.odometry.update();
    }
}
