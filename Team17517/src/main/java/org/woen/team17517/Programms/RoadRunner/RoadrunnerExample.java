package org.woen.team17517.Programms.RoadRunner;

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

import org.woen.team17517.RobotModules.UltRobot;

import java.util.Arrays;
import java.util.List;

@TeleOp
public class RoadrunnerExample extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new UltRobot(this);
        waitForStart();
        robot.mover.on();
        while (opModeIsActive()) {
            robot.updateWhilePositionFalse(
                    () -> robot.mover.trajectories(robot.mover.builder().strafeTo(new Vector2d(50, 100)).build())
            );
            robot.allUpdate();
        }
    }
}
