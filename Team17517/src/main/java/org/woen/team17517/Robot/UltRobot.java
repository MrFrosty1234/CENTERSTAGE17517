package org.woen.team17517.Robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Robot.OpenCV.TestAprilTagPipeline;
import org.woen.team17517.Robot.OpenCV.UpdateCameraAndOdometry;

import java.util.List;


public class UltRobot {
    public DriveTrain driveTrain;
    public DrivetrainNew drivetrainNew;
    public Grabber grabber;
    public Lift lift;
    public Lighting lighting;
    public VoltageSensorPoint voltageSensorPoint;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public TestAprilTagPipeline testAprilTagPipeline;
    public TelemetryOutput telemetryOutput;
    public DriveTrainVelocityControl driveTrainVelocityControl;
    public Odometry odometry;
    public Timer timer;
    public RobotModule[] robotModules;
    private final List<LynxModule> revHubs;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        telemetryOutput = new TelemetryOutput(this);
        timer = new Timer(this);
        driveTrain = new DriveTrain(this);
        grabber = new Grabber(this);
        voltageSensorPoint = new VoltageSensorPoint(this);
        lift = new Lift(this);
        driveTrainVelocityControl = new DriveTrainVelocityControl(this);
        gyro = new Gyro(this);
        lighting = new Lighting(this);
        testAprilTagPipeline = new TestAprilTagPipeline(this);
        odometry = new Odometry(this);
        drivetrainNew = new DrivetrainNew(this);
        this.robotModules = new RobotModule[]{telemetryOutput, grabber, timer, voltageSensorPoint,
                lift, driveTrainVelocityControl, gyro, lighting, odometry, drivetrainNew};
        revHubs = linearOpMode.hardwareMap.getAll(LynxModule.class);
        revHubs.forEach(it -> it.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
    }
    public boolean isAtPositionAll(){
        boolean positionIndicator = true;
        for(RobotModule robotModule : robotModules){
            positionIndicator &= robotModule.isAtPosition();
        }
        return positionIndicator;
    }
    public void allUpdate() {
        revHubs.forEach(it -> it.clearBulkCache());
        for(RobotModule robotModule: robotModules){
            robotModule.update();
        }
    }

    public void updateWhilePositionFalse(Runnable[] runnables){
        for (Runnable runnable : runnables){
            allUpdate();
            runnable.run();

            double oldTime = System.currentTimeMillis();

            while(!isAtPositionAll() && System.currentTimeMillis()-oldTime<5000 && linearOpMode.opModeIsActive()){
                allUpdate();
            }

        }
    }
}
