package org.woen.team17517.Programms.RoadRunner;

import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.RobotModules.UltRobot;

import java.lang.reflect.Array;
import java.util.Arrays;

@TeleOp
public class RoadrunnerTest extends LinearOpMode {
    UltRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new UltRobot(this);
        waitForStart();
        robot.mover.on();
        if (opModeIsActive()) {
            robot.updateWhilePositionFalse(new Runnable[]{
                    ()-> robot.mover.trajectories(
                            robot.mover.builder()
                                    .splineToLinearHeading(new Pose2d(-50, 0, Math.toRadians(0)), Math.toRadians(0))
                                    .splineToLinearHeading(new Pose2d(-30, 0, Math.toRadians(0)), Math.toRadians(0))
                                    .splineToLinearHeading(new Pose2d(-30, -35, Math.toRadians(90)), 0)
                                    .build()),
                    ()-> robot.mover.trajectories(
                            robot.mover.builder()
                                    .splineToConstantHeading(new Vector2d(-40,-95), Math.toRadians(90))
                                    .splineToConstantHeading(new Vector2d(-120, -45), Math.toRadians(90))
                                    .splineToConstantHeading(new Vector2d(-110,95),Math.toRadians(90))
                                    .build()),
                    ()-> robot.mover.trajectories(
                            robot.mover.builder()
                                    .splineToConstantHeading(new Vector2d(-110,-45), Math.toRadians(90))
                                    .splineToConstantHeading(new Vector2d(-60, -65), Math.toRadians(90))
                                    .build()),
            });
        }
    }
}
