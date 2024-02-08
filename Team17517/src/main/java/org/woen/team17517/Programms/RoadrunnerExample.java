package org.woen.team17517.Programms;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Rotation2dDual;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.List;

@TeleOp
public class RoadrunnerExample extends LinearOpMode {

    double wheelDiameter = 9.6;
    double lateralMultiplier = 1.2;

    double maxTurnSpeed = toRadians(180);
    double maxTurnAccel = toRadians(360);

    double kPForward = .0;
    double kPSide = .0;
    double kPTurn = .0;

    double maxRobotSpeed = 100;

    double minAccel = -150;
    double maxAccel = 150;

    private final MecanumKinematics mecanumKinematics = new MecanumKinematics(wheelDiameter, lateralMultiplier);
    private final TurnConstraints turnConstraints = new TurnConstraints(maxTurnSpeed, -maxTurnSpeed, maxTurnAccel);
    private final VelConstraint velConstraint = new MinVelConstraint(Arrays.asList(mecanumKinematics.new WheelVelConstraint(maxRobotSpeed), new AngularVelConstraint(maxTurnSpeed)));
    private final AccelConstraint accelConstraint = new ProfileAccelConstraint(minAccel, maxAccel);


    public void runOpMode(){
        Pose2d beginPose = new Pose2d(0,0,0);


        TrajectoryBuilder trajectoryBuilder = new TrajectoryBuilder(beginPose,1e-6,0, velConstraint, accelConstraint,0.25,0.1);
        List<Trajectory> trajectories = trajectoryBuilder
                //.splineTo(new Vector2d(0,0),0)
                //.strafeTo()
                //.lineToX()
                .build();

        ElapsedTime trajectoryTime = new ElapsedTime();
        for(Trajectory trajectory: trajectories) {
            trajectoryTime.reset();
            double timeSeconds;
            TimeTrajectory timeTrajectory = new TimeTrajectory(trajectory);
            double trajectoryDuration = timeTrajectory.duration;
            do {
                timeSeconds = trajectoryTime.seconds();
                Pose2dDual<Time> targetPose = timeTrajectory.get(timeSeconds);
                Vector2dDual<Time> targetVector = targetPose.position;
                Rotation2dDual<Time> heading = targetPose.heading;
                Vector2dDual<Time> linearVel = targetPose.velocity().linearVel;
                DualNum<Time> angVel = targetPose.velocity().angVel;

                /* double targetX = targetVector.x.value();
                double targetY = targetVector.y.value();
                double targetHeading = heading.value().toDouble(); //in global coords
                double velForward = linearVel.x.value();
                double velSide = linearVel.y.value();
                double velHeading = angVel.value(); //in robot coords */

                double robotPoseX = 0; //TODO in global coords
                double robotPoseY = 0; //TODO
                double robotPoseH = 0; //TODO
                Pose2d robotPose = new Pose2d(robotPoseX,robotPoseY,robotPoseH);

                double robotVelForward = 0; //TODO in robot coords
                double robotVelSide = 0;
                double robotVelTurn = 0;
                PoseVelocity2d robotVelocity = new PoseVelocity2d(new Vector2d(robotVelForward, robotVelSide), robotVelTurn);
                HolonomicController holonomicController = new HolonomicController(kPForward,kPSide,kPTurn);
                PoseVelocity2dDual<Time> targetVelocity = holonomicController.compute(targetPose,robotPose,robotVelocity);
                double targetVelForward = targetVelocity.linearVel.x.value();
                double targetVelSide = targetVelocity.linearVel.y.value();
                double targetVelTurn = targetVelocity.angVel.value();
                //drivetrainVelocity.setCMSpeed(targetVelForward, targetVelSide, targetVelTurn)
            } while (timeSeconds < trajectoryDuration && opModeIsActive());
        }



    }

}
