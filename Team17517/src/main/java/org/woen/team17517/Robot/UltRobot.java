package org.woen.team17517.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class UltRobot {
    public DriveTrain driveTrain;
    public Grabber grabber;
    public Lift lift;
    public Lighting lighting;
    public VoltageSensorPoint voltageSensorPoint;
    public Odometry odometry;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public Telemetry telemetry;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        driveTrain = new DriveTrain(this);
        grabber = new Grabber(this);
        lift = new Lift(this);

        voltageSensorPoint = new VoltageSensorPoint(this);
        gyro = new Gyro(this);
        lighting = new Lighting(this);
        odometry = new Odometry(this);
    }

    public void allUpdate() {
        lift.update();
        lighting.update();
        odometry.update();
        gyro.update();
        grabber.update();
        telemetry.update();
        voltageSensorPoint.update();
    }
}
