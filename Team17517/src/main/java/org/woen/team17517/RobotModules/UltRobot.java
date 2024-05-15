package org.woen.team17517.RobotModules;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Devices.Hanging;
import org.woen.team17517.RobotModules.DriveTrain.Mover;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrain;
import org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl;
import org.woen.team17517.RobotModules.EndGame.Hang;
import org.woen.team17517.RobotModules.EndGame.Plane;
import org.woen.team17517.RobotModules.Intake.Grabber.Brush;
import org.woen.team17517.RobotModules.Intake.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Intake.Grabber.OpticalSensor;
import org.woen.team17517.RobotModules.Intake.Lift.Lift;
import org.woen.team17517.RobotModules.Lighting.Lighting;
import org.woen.team17517.RobotModules.Navigative.Gyro;
import org.woen.team17517.RobotModules.Navigative.Localization;
import org.woen.team17517.RobotModules.OpenCV.TestAprilTagPipeline;
import java.util.List;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.TelemetryOutput;
import org.woen.team17517.Service.Timer;
import org.woen.team17517.Service.VoltageSensorPoint;

import org.woen.team17517.Devices.Hardware;

import org.woen.team17517.RobotModules.Intake.Intake;


public class UltRobot {
    public DriveTrain driveTrain;
    public Lift lift;
    public GrabberNew grabber;
    public OpticalSensor opticalSensor;
    public Lighting lighting;
    public VoltageSensorPoint voltageSensorPoint;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public TestAprilTagPipeline testAprilTagPipeline;
    public TelemetryOutput telemetryOutput;
    public DriveTrainVelocityControl driveTrainVelocityControl;
    public Localization odometry;
    public Timer timer;
    public RobotModule[] robotModules;
    public Hardware hardware;
    public Mover mover;
    public Brush brush;
    public Plane plane;
    public Intake intake;
    public Hang hang;
    private final List<LynxModule> revHubs;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        hardware = new Hardware(linearOpMode1.hardwareMap);
        telemetryOutput = new TelemetryOutput(this);
        timer = new Timer(this);
        lift = new Lift(this);
        grabber = new GrabberNew(this);
        opticalSensor = new OpticalSensor(this);
        brush = new Brush(this);
        voltageSensorPoint = new VoltageSensorPoint(this);
        driveTrainVelocityControl = new DriveTrainVelocityControl(this);
        gyro = new Gyro(this);
        lighting = new Lighting(this);
        testAprilTagPipeline = new TestAprilTagPipeline(this);
        hang = new Hang(this);
        intake = new Intake(this);
        odometry = new Localization(this);
        driveTrain = new DriveTrain(this);
        plane = new Plane(this);
        mover= new Mover(this);

        this.robotModules = new RobotModule[]{odometry,mover,driveTrain,driveTrainVelocityControl,gyro,
                opticalSensor,intake,lift,grabber,brush,hang
                ,lighting,voltageSensorPoint,telemetryOutput,timer};
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

    public void updateWhilePositionFalseTimer(double time,Runnable...runnables){
        for (Runnable runnable : runnables){
            runnable.run();
            allUpdate();
            double startTime = (double) System.currentTimeMillis() /1000d;


            while(!isAtPositionAll() && linearOpMode.opModeIsActive()&&((double)   System.currentTimeMillis() /1000d - startTime)<time){
                allUpdate();
                linearOpMode.telemetry.addData("posAll", isAtPositionAll());
            }
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
