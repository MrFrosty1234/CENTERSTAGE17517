package org.woen.team17517.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class UltRobot {
    public DriveTrain driveTrain;
    public Grabber grabber;
    public Lift lift;
    public Lighting lighting;
    public Odometry odometry;
    public LinearOpMode linearOpMode;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        driveTrain = new DriveTrain(this);
        grabber = new Grabber(this);
        lift = new Lift(this);

        lighting = new Lighting(this);
        odometry = new Odometry(this);
    }

    public void allUpdate() {
        lift.update();
        lighting.update();
        odometry.update();
    }
}
