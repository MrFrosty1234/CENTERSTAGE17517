package org.woen.meepmeep17517;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Arrays;

public class MeepMeep17517 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        double kPForward = .0;
        double kPSide = .0;
        double kPTurn = .0;
        double wheelDiameter = 9.6;
        double xMultiplier = 1.2;
        MecanumKinematics kinematics = new MecanumKinematics(wheelDiameter, xMultiplier);
        double maxVel = 400d;
        double maxAccel = 400d;
        double maxAngVel = Math.toRadians(270d);
        double maxAngAccel = Math.toRadians(270d);
        double trackWidth = 35d;
        final double INCH_TO_CM = 1d / 2.54d;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(maxVel * INCH_TO_CM, maxAccel * INCH_TO_CM, maxAngVel, maxAngAccel, trackWidth * INCH_TO_CM)
                .build();

        myBot.runAction(myBot.getDrive()
                .actionBuilder(new Pose2d(-34,56,Math.toRadians(-90)))
                                .strafeTo(new Vector2d(-34,30))
                                .splineToLinearHeading(new Pose2d(-60,24,Math.toRadians(180)),0, new MinVelConstraint(
                                      Arrays.asList(kinematics.new WheelVelConstraint(68000),new AngularVelConstraint(Math.toRadians(80)))
                                ))
                        .splineToLinearHeading(new Pose2d(-16,5,Math.toRadians(180)),0, new MinVelConstraint(
                        Arrays.asList(kinematics.new WheelVelConstraint(68000),new AngularVelConstraint(Math.toRadians(100)))
                )).endTrajectory()
                        .strafeTo(new Vector2d(52,30))
                .splineToLinearHeading(new Pose2d(20,10,Math.toRadians(180)),0, new MinVelConstraint(
                        Arrays.asList(kinematics.new WheelVelConstraint(68000),new AngularVelConstraint(Math.toRadians(40)))
                ))
                .splineToLinearHeading(new Pose2d(-50,10,Math.toRadians(180)),0, new MinVelConstraint(
                        Arrays.asList(kinematics.new WheelVelConstraint(100),new AngularVelConstraint(Math.toRadians(40)))
                ))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.5f)
                .addEntity(myBot)
                .start();
    }
}