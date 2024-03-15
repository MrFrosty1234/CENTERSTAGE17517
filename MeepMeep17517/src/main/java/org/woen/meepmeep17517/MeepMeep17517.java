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
        double maxVel = 250d;
        double maxAccel = 250d;
        double maxAngVel = Math.toRadians(270d);
        double maxAngAccel = Math.toRadians(270d);
        double trackWidth = 35d;
        final double INCH_TO_CM = 1d / 2.54d;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(maxVel * INCH_TO_CM, maxAccel * INCH_TO_CM, maxAngVel, maxAngAccel, trackWidth * INCH_TO_CM)
                .build();

        myBot.runAction(myBot.getDrive()
                .actionBuilder(new Pose2d(12, 70, Math.toRadians(-90)))
                .strafeTo(new Vector2d(12, 32.5))
                .strafeTo(new Vector2d(12, 35))
                .splineToLinearHeading(new Pose2d(42, 35, Math.toRadians(0)), 0)
                .endTrajectory()
                .splineToConstantHeading(new Vector2d(30, 13), -30)
                .strafeTo(new Vector2d(-55, 11))
                .endTrajectory()
                .strafeTo(new Vector2d(30, 13))
                .splineToConstantHeading(new Vector2d(50, 33), 0)
                .endTrajectory()
                .strafeTo(new Vector2d(30, 13))
                .strafeTo(new Vector2d(-55, 11))
                .endTrajectory()
                .strafeTo(new Vector2d(30, 13))
                .splineToConstantHeading(new Vector2d(50, 33), 0)
                .endTrajectory()
                .strafeTo(new Vector2d(30, 13))
                .strafeTo(new Vector2d(-55, 11))
                .endTrajectory()
                .strafeTo(new Vector2d(30, 13))
                .splineToConstantHeading(new Vector2d(50, 33), 0)
                .endTrajectory()
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}