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
        MeepMeep meepMeep = new MeepMeep(800);
        double maxVel = 200d;
        double maxAccel = 200d;
        double maxAngVel = Math.toRadians(270d);
        double maxAngAccel = Math.toRadians(270d);
        double trackWidth = 35d;
        final double INCH_TO_CM = 1d / 2.54d;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(maxVel * INCH_TO_CM, maxAccel * INCH_TO_CM, maxAngVel, maxAngAccel, trackWidth * INCH_TO_CM)
                .build();

        myBot.runAction(myBot.getDrive()
                .actionBuilder(new Pose2d(12,70,Math.toRadians(-90)))
                .strafeTo(new Vector2d(12,32.5))
                .strafeTo(new Vector2d(12,47))
                .splineToLinearHeading(new Pose2d(42,35,Math.toRadians(180)),-1)
                .endTrajectory()
                .strafeTo(new Vector2d(47,24))
                .splineToConstantHeading(new Vector2d(0,12),-3)
                .splineToConstantHeading(new Vector2d(-54,12),0)
                .endTrajectory()
                .splineToConstantHeading(new Vector2d(0,12),0)
                .splineToConstantHeading(new Vector2d(47,14),0)
                .strafeToConstantHeading(new Vector2d(42,35))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}