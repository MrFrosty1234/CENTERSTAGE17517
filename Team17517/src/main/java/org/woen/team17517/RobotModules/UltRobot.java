package org.woen.team17517.RobotModules;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.NotUsedCode.DrivetrainNew;
import org.woen.team17517.NotUsedCode.Odometry;
import org.woen.team17517.Programms.Autonomus.OldAutonomus.AutnomModules;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrain;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl;
import org.woen.team17517.RobotModules.Transport.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Transport.Grabber.PixelsCount;
import org.woen.team17517.RobotModules.Transport.Lift.Lift;
import org.woen.team17517.RobotModules.Lighting.Lighting;
import org.woen.team17517.RobotModules.Navigative.Gyro;
import org.woen.team17517.RobotModules.Navigative.OdometryNew;
import org.woen.team17517.RobotModules.OpenCV.TestAprilTagPipeline;
import java.util.List;

import org.woen.team17517.RobotModules.Transport.TransportPixels;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.TelemetryOutput;
import org.woen.team17517.Service.Timer;
import org.woen.team17517.Service.VoltageSensorPoint;


public class UltRobot {
    public DriveTrain driveTrain;
    public DrivetrainNew drivetrainNew;
    public TransportPixels transportPixels;
    private Lift lift;
    public GrabberNew grabber;
    private PixelsCount pixelsCount;
    public Lighting lighting;
    public VoltageSensorPoint voltageSensorPoint;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public TestAprilTagPipeline testAprilTagPipeline;
    public TelemetryOutput telemetryOutput;
    public DriveTrainVelocityControl driveTrainVelocityControl;
    public Odometry odometry;
    public OdometryNew odometryNew;
    public Devices devices;
    public Timer timer;
    public AutnomModules autnomModules;
    public RobotModule[] robotModules;
    private final List<LynxModule> revHubs;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        autnomModules  = new AutnomModules(this);
        devices = new Devices(this);
        telemetryOutput = new TelemetryOutput(this);
        timer = new Timer(this);
        transportPixels = new TransportPixels(lift, grabber,pixelsCount);
        voltageSensorPoint = new VoltageSensorPoint(this);
        grabber = new GrabberNew(this);
        lift = new Lift(this);
        pixelsCount = new PixelsCount(this);
        driveTrainVelocityControl = new DriveTrainVelocityControl(this);
        gyro = new Gyro(this);
        lighting = new Lighting(this);
        testAprilTagPipeline = new TestAprilTagPipeline(this);
        odometry = new Odometry(this);
        drivetrainNew = new DrivetrainNew(this);
        odometryNew = new OdometryNew(this);
        driveTrain = new DriveTrain(this);
        this.robotModules = new RobotModule[]{telemetryOutput,timer, voltageSensorPoint,
                 driveTrainVelocityControl, transportPixels, gyro, lighting, odometryNew, drivetrainNew};
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

    public static void updateOneWhileOneRobotModule(RobotModule run , Runnable [] runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            run.update();

            while(!run.isAtPosition()){
                run.update();
            }
        }

    }
    public  void updateAllWhileOneRobotModule(RobotModule run , Runnable [] runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            allUpdate();

            while(!run.isAtPosition()){
                allUpdate();
            }
        }

    }

    public void updateWhilePositionFalse(Runnable[] runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            allUpdate();

            double oldTime = System.currentTimeMillis();

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
