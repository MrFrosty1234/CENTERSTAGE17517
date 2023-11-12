package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.FtcDashboard;
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
    public TelemetryOutput telemetryOutput;
    public DriveTrainVelocityControl driveTrainVelocityControl;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        telemetryOutput = new TelemetryOutput(this);
        driveTrain = new DriveTrain(this);
        grabber = new Grabber(this);
        voltageSensorPoint = new VoltageSensorPoint(this);
        lift = new Lift(this);
        driveTrainVelocityControl  = new DriveTrainVelocityControl(this);
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
        telemetryOutput.update();
        voltageSensorPoint.update();
        driveTrainVelocityControl.update();
    }
}
