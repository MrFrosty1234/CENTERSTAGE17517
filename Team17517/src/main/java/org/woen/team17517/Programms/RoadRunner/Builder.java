package org.woen.team17517.Programms.RoadRunner;

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

import java.util.*;

public class Builder{
    public Builder(UltRobot robot){
        this.robot = robot;
    }
    UltRobot robot;
    private double kPForward = .0;
    private double kPSide = .0;
    private double kPTurn = .0;
    private final double wheelDiameter = 9.6;
    private final double xMultiplier = 1.2;
    private final double maxAccel = 2000;
    private final double minAccel = -2000;
    private final MecanumKinematics kinematics = new MecanumKinematics(wheelDiameter,xMultiplier);
    private final Pose2d beginPose = new Pose2d(0,0,0);
    private final VelConstraint velConstraint = new MinVelConstraint(Arrays.asList(kinematics.
            new WheelVelConstraint(DriveTrainVelocityControl.maxLinearSpeedOd),
            new AngularVelConstraint(DriveTrainVelocityControl.maxAngleSpeedOd)
    ));
    private final AccelConstraint accelConstraint = new ProfileAccelConstraint(minAccel,maxAccel);
    protected final TrajectoryBuilder builder = new TrajectoryBuilder(beginPose,1e-6,0, velConstraint,
                                                                    accelConstraint,0.25,0.1);
    public TrajectoryBuilder builder(){return builder;}
    public void move(List<Trajectory> trajectories){
        ElapsedTime time = new ElapsedTime();
        for (Trajectory trajectory:trajectories) {
            time.reset();
            TimeTrajectory timeTrajectory = new TimeTrajectory(trajectory);
            double duration = timeTrajectory.duration;
            do{
                Pose2dDual target = timeTrajectory.get(time.seconds());
                Pose2d pose = new Pose2d(robot.odometry.getGlobalPosX(),robot.odometry.getGlobalPosY(),
                        Math.toRadians(robot.odometry.getGlobalAngle()));
                PoseVelocity2d velocity = new PoseVelocity2d(
                        new Vector2d(robot.odometry.getVelLocalX(),robot.odometry.getVelLocalY())
                        ,robot.odometry.getVelLocalH());
                HolonomicController controller = new HolonomicController(kPForward,kPSide,kPTurn);
                PoseVelocity2dDual<Time> targetVelocity = controller.compute(target,pose,velocity);
                robot.driveTrainVelocityControl.moveRobotCord(
                        targetVelocity.linearVel.y.value(),
                        targetVelocity.linearVel.x.value(),
                        targetVelocity.angVel.value()
                );
                robot.allUpdate();
            }while (time.seconds()<duration);
        }
    }
}
