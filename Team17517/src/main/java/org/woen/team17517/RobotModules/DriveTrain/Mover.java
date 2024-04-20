package org.woen.team17517.RobotModules.DriveTrain;

import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.VEL_ANGLE_TO_ENC;
import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.ENC_TO_SM;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

import java.util.*;
@Config
public class Mover implements RobotModule {
    public static double maxAccel = 5;
    public static double minAccel = -5;

    public static double maxLinSpeed = 15;
    public static double maxAngSpeed = 0.1;
    public Mover(@NonNull UltRobot robot)
    {
        this.robot = robot;
        robot.hardware.odometers.reset();
        Pose2d beginPose = new Pose2d(robot.odometry.getGlobalPositionVector().convertToSmFromEnc()
                .convertToVector2d(),toRadians(robot.odometry.getGlobalAngle()));
        double wheelDiameter = 9.6;
        double xMultiplier = 1.2;
        MecanumKinematics kinematics = new MecanumKinematics(wheelDiameter, xMultiplier);
        VelConstraint velConstraint = new MinVelConstraint(Arrays.asList(kinematics.
                        new WheelVelConstraint(maxLinSpeed),
                new AngularVelConstraint(maxAngSpeed)
        ));
        AccelConstraint accelConstraint = new ProfileAccelConstraint(minAccel, maxAccel);
        builder = new TrajectoryBuilder(beginPose, 1e-6, 0, velConstraint,
                accelConstraint, 0.25, 0.1);
    }

    UltRobot robot;
    public static double kPForward = 2300;
    public static double kPSide = 2300;
    public static double kPTurn = 4;
    protected final TrajectoryBuilder builder;
    public TrajectoryBuilder builder() {
        return builder;
    }
    public void trajectories(List<Trajectory> trajectories) {
        this.trajectories = trajectories;
    }
    ElapsedTime time = new ElapsedTime();
    private Pose2dDual<Time> end;
    private boolean isOn = false;
    public void on(){isOn = true;}
    public void off(){isOn = false;}
    private List<Trajectory> trajectories = new ArrayList<>();
    private double error = 0;
    private double errorHeading = 0;
    public Pose2d getPose() {
        return pose;
    }

    public PoseVelocity2d getVelocity() {
        return velocity;
    }

    Pose2d pose = new Pose2d(0,0,0);
    PoseVelocity2d velocity = new PoseVelocity2d(new Vector2d(0,0),0);
    @Override
    public void update() {
        if(isOn){
            pose = new Pose2d(robot.odometry.getGlobalPositionVector().convertToSmFromEnc().convertToVector2d(),
                    toRadians(robot.odometry.getGlobalAngle()));
            velocity = new PoseVelocity2d(robot.odometry.getLocalVelocityVector().convertToSmFromEnc().convertToVector2d(),
                    toRadians(robot.odometry.getVelLocalH()));
            HolonomicController controller = new HolonomicController(kPForward, kPSide, kPTurn);
            if(!trajectories.isEmpty()) {
                Trajectory trajectory = trajectories.get(0);
                TimeTrajectory timeTrajectory = new TimeTrajectory(trajectory);
                double duration = timeTrajectory.duration;
                end = timeTrajectory.get(duration);
                error        = timeTrajectory.get(duration).value().position.minus(pose.position).norm();
                errorHeading = timeTrajectory.get(duration).value().heading.toDouble() - pose.heading.toDouble();
                Pose2dDual<Time> target = timeTrajectory.get(time.seconds());
                PoseVelocity2dDual<Time> targetVelocity = controller.compute(target, pose, velocity);
                robot.driveTrainVelocityControl.moveRobotCord(
                        -targetVelocity.linearVel.y.value()/ENC_TO_SM,
                        targetVelocity.linearVel.x.value()/ENC_TO_SM,
                        -toDegrees(targetVelocity.angVel.value())*VEL_ANGLE_TO_ENC
                );
                if(isAtPosition())trajectories.remove(0);
            }else if (end!=null){
                PoseVelocity2dDual<Time> targetVelocity = controller.compute(end, pose, velocity);
                robot.driveTrainVelocityControl.moveRobotCord(
                        targetVelocity.linearVel.y.value(),
                        targetVelocity.linearVel.x.value(),
                        targetVelocity.angVel.value()
                );
            }
        }
    }

    @Override
    public boolean isAtPosition() {
        return !isOn || (abs(errorHeading) < 0.1 && abs(error) < 5);
    }
}
