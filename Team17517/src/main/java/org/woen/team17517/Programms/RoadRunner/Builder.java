package org.woen.team17517.Programms.RoadRunner;

import static java.lang.Math.abs;

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

import org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

import java.util.*;

public class Builder implements RobotModule {
    public Builder(UltRobot robot) {
        this.robot = robot;
    }

    UltRobot robot;
    public static double kPForward = .0;
    public static double kPSide = .0;
    public static double kPTurn = .0;
    private final double wheelDiameter = 9.6;
    private final double xMultiplier = 1.2;
    private final double maxAccel = 2000;
    private final double minAccel = -2000;
    private final MecanumKinematics kinematics = new MecanumKinematics(wheelDiameter, xMultiplier);
    private final Pose2d beginPose = new Pose2d(0, 0, 0);
    private final VelConstraint velConstraint = new MinVelConstraint(Arrays.asList(kinematics.
                    new WheelVelConstraint(DriveTrainVelocityControl.maxLinearSpeedOd),
            new AngularVelConstraint(DriveTrainVelocityControl.maxAngleSpeedOd)
    ));
    private final AccelConstraint accelConstraint = new ProfileAccelConstraint(minAccel, maxAccel);
    protected final TrajectoryBuilder builder = new TrajectoryBuilder(beginPose, 1e-6, 0, velConstraint,
            accelConstraint, 0.25, 0.1);

    public TrajectoryBuilder builder() {
        return builder;
    }

    private List<Trajectory> trajectories;

    public void trajectories(List<Trajectory> trajectories) {
        this.trajectories = trajectories;
    }

    private boolean isAtPosition = false;
    ElapsedTime time = new ElapsedTime();

    private Pose2dDual<Time> end = null;
    @Override
    public void update() {
        Trajectory trajectory = null;
        Pose2d pose = new Pose2d(robot.odometry.getGlobalPosX(), robot.odometry.getGlobalPosY(),
                Math.toRadians(robot.odometry.getGlobalAngle()));
        PoseVelocity2d velocity = new PoseVelocity2d(
                new Vector2d(robot.odometry.getVelLocalX(), robot.odometry.getVelLocalY()),
                robot.odometry.getVelLocalH());
        HolonomicController controller = new HolonomicController(kPSide, kPForward, kPTurn);
        if (trajectories != null && !trajectories.isEmpty()) {
            isAtPosition = false;
            trajectory = trajectories.get(0);
            TimeTrajectory timeTrajectory = new TimeTrajectory(trajectory);
            double duration = timeTrajectory.duration;
            end = timeTrajectory.get(duration);
            double error = timeTrajectory.get(duration).value().position.minus(pose.position).norm();
            double errorHeading = timeTrajectory.get(duration).value().heading.real - pose.heading.real;
            Pose2dDual<Time> target = timeTrajectory.get(time.seconds());
            PoseVelocity2dDual<Time> targetVelocity = controller.compute(target, pose, velocity);
            robot.driveTrainVelocityControl.moveRobotCord(
                    targetVelocity.linearVel.y.value(),
                    targetVelocity.linearVel.x.value(),
                    targetVelocity.angVel.value()
            );
            if (abs(errorHeading) < 0.1 && abs(error) < 1000) trajectories.remove(0);isAtPosition = true;
        }else{
            isAtPosition = true;
            if(end!=null) {
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
        return isAtPosition;

    }
}
