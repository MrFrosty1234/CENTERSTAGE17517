package org.woen.team17517.RobotModules;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.NotUsedCode.DrivetrainNew;
import org.woen.team17517.NotUsedCode.Odometry;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrain;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl;
import org.woen.team17517.RobotModules.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Lift.Lift;
import org.woen.team17517.RobotModules.Lighting.Lighting;
import org.woen.team17517.RobotModules.Navigative.Gyro;
import org.woen.team17517.RobotModules.Navigative.OdometryNew;
import org.woen.team17517.RobotModules.OpenCV.TestAprilTagPipeline;
import java.util.List;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.TelemetryOutput;
import org.woen.team17517.Service.Timer;
import org.woen.team17517.Service.VoltageSensorPoint;

import Devices.Hardware;


public class UltRobot {
    public DriveTrain driveTrain;
    public DrivetrainNew drivetrainNew;
    public Lift lift;
    public GrabberNew grabber;
    public PixelsCount pixelsCount;
    public Lighting lighting;
    public VoltageSensorPoint voltageSensorPoint;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public TestAprilTagPipeline testAprilTagPipeline;
    public TelemetryOutput telemetryOutput;
    public DriveTrainVelocityControl driveTrainVelocityControl;
    public Odometry odometry;
    public OdometryNew odometryNew;
    public Timer timer;
    public RobotModule[] robotModules;
    public Hardware hardware;
    private final List<LynxModule> revHubs;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        //devices = new Devices(this);
        hardware = new Hardware(linearOpMode1.hardwareMap);
        telemetryOutput = new TelemetryOutput(this);
        timer = new Timer(this);
        lift = new Lift(this);
        grabber = new GrabberNew(this);
        pixelsCount = new PixelsCount(this);
        voltageSensorPoint = new VoltageSensorPoint(this);
        driveTrainVelocityControl = new DriveTrainVelocityControl(this);
        gyro = new Gyro(this);
        lighting = new Lighting(this);
        testAprilTagPipeline = new TestAprilTagPipeline(this);
        odometry = new Odometry(this);
        drivetrainNew = new DrivetrainNew(this);
        odometryNew = new OdometryNew(this);
        driveTrain = new DriveTrain(this);
        this.robotModules = new RobotModule[]{telemetryOutput,timer, voltageSensorPoint,
                 driveTrainVelocityControl,lift,grabber,pixelsCount, gyro, lighting, odometryNew, drivetrainNew};
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

    public void updateWhilePositionFalseTimer(double time,Runnable[] runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            allUpdate();
            double startTime = (double) System.currentTimeMillis() /1000d;


            while(!isAtPositionAll() && linearOpMode.opModeIsActive()&&((double)   System.currentTimeMillis() /1000d - startTime)<time){
                linearOpMode.telemetry.addData("posAll", isAtPositionAll());
                linearOpMode.telemetry.update();
                allUpdate();
                linearOpMode.telemetry.addData("posAll", isAtPositionAll());
                linearOpMode.telemetry.update();
            }
            linearOpMode.telemetry.addData("posAll", isAtPositionAll());
            linearOpMode.telemetry.update();
        }

    }
    public void updateWhilePositionFalse(Runnable[] runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            allUpdate();


            while(!isAtPositionAll() && linearOpMode.opModeIsActive()){
                linearOpMode.telemetry.addData("posAll", isAtPositionAll());
                linearOpMode.telemetry.update();
                allUpdate();
                linearOpMode.telemetry.addData("posAll", isAtPositionAll());
                linearOpMode.telemetry.update();
            }
            linearOpMode.telemetry.addData("posAll", isAtPositionAll());
            linearOpMode.telemetry.update();
        }

    }
}
