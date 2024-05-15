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
    public static double maxAccel = 100;
    public static double minAccel = -100;

    public static double maxLinSpeed = 100;
    public static double maxAngSpeed = 10;

    public Mover(@NonNull UltRobot robot) {
        this.robot = robot;
        robot.hardware.odometers.reset();
        double wheelDiameter = 9.6;
        double xMultiplier = 1.2;
        kinematics = new MecanumKinematics(wheelDiameter, xMultiplier);
        velConstraint = new MinVelConstraint(Arrays.asList(kinematics.
                        new WheelVelConstraint(maxLinSpeed),
                new AngularVelConstraint(maxAngSpeed)
        ));
        accelConstraint = new ProfileAccelConstraint(minAccel, maxAccel);
    }

    UltRobot robot;
    VelConstraint velConstraint;
    MecanumKinematics kinematics;
    AccelConstraint accelConstraint;
    public static double kPForward = 0;
    public static double kPSide = 0;
    public static double kPTurn = 0;
    protected  TrajectoryBuilder builder;

    public TrajectoryBuilder builder() {
        return new TrajectoryBuilder(new Pose2d(robot.odometry.getGlobalPositionVector().convertToVector2d(),
                toRadians(robot.odometry.getGlobalAngle())), 1e-6, 0, velConstraint,
                accelConstraint, 0.25, 0.1);
    }

    public void trajectories(List<Trajectory> trajectories) {
        this.trajectories = trajectories;
        reset();
    }

    ElapsedTime time = new ElapsedTime();

    public void reset() {
        time.reset();
    }

    private Pose2dDual<Time> end;
    private boolean isOn = false;

    public void on() {
        isOn = true;
    }

    public void off() {
        isOn = false;
    }

    private List<Trajectory> trajectories = new ArrayList<>();
    private double error = 0;
    private double errorHeading = 0;

    public Pose2d getPose() {
        return pose;
    }

    public double getError() {
        return error;
    }

    public double getErrorHeading() {
        return errorHeading;
    }

    public PoseVelocity2d getVelocity() {
        return velocity;
    }

    Pose2d pose = new Pose2d(0, 0, 0);
    PoseVelocity2d velocity = new PoseVelocity2d(new Vector2d(0, 0), 0);
    private boolean isFirst = true;
    private double duration;

    @Override
    public void update() {
        if (isOn) {
            pose = new Pose2d(robot.odometry.getGlobalPositionVector().convertToVector2d(),
                    toRadians(robot.odometry.getGlobalAngle()));
            velocity = new PoseVelocity2d(robot.odometry.getLocalVelocityVector().convertToVector2d(),
                    toRadians(robot.odometry.getVelLocalH()));
            HolonomicController controller = new HolonomicController(kPForward, kPSide, kPTurn);
            if (!trajectories.isEmpty()) {
                isFirst = false;
                Trajectory trajectory = trajectories.get(0);
                TimeTrajectory timeTrajectory = new TimeTrajectory(trajectory);
                duration = timeTrajectory.duration;
                end = timeTrajectory.get(duration);
                error = timeTrajectory.get(duration).value().position.minus(pose.position).norm();
                errorHeading = timeTrajectory.get(duration).value().heading.toDouble() - pose.heading.toDouble();
                Pose2dDual<Time> target = timeTrajectory.get(time.seconds());
                PoseVelocity2dDual<Time> targetVelocity = controller.compute(target, pose, velocity);
                robot.driveTrainVelocityControl.moveRobotCord(
                        -targetVelocity.linearVel.y.value() / ENC_TO_SM,
                        targetVelocity.linearVel.x.value() / ENC_TO_SM,
                        -toDegrees(targetVelocity.angVel.value()) * VEL_ANGLE_TO_ENC
                );
                if (time.seconds()>duration) {
                    trajectories.remove(0);
                    reset();
                }
            } else if (end != null) {
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
        if(isOn) {
            if (trajectories != null) {
                if (trajectories.isEmpty()) {
                    return true;
                }
            }
        }
        return false;

    }
}
