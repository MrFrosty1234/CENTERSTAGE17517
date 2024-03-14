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
                .actionBuilder(new Pose2d(12,70,Math.toRadians(-90)))
                                .strafeTo(new Vector2d(12,32.5))
                                .strafeTo(new Vector2d(12,47))
                                .splineToLinearHeading(new Pose2d(42,35,Math.toRadians(180)),-1)
                                .splineToLinearHeading(new Pose2d(35,20,Math.toRadians(180)),5)
                                .splineToLinearHeading(new Pose2d(-52,11,Math.toRadians(180)),-10, new MinVelConstraint(
                                      Arrays.asList(kinematics.new WheelVelConstraint(100),new AngularVelConstraint(Math.toRadians(10)))
                                ))

                //.splineTo(new Vector2d(100 * INCH_TO_CM, 30 * INCH_TO_CM), 0)
                //.lineToX(30 * INCH_TO_CM)
                //.turn(PI / 2)
                //.lineToY(-30 * INCH_TO_CM)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.5f)
                .addEntity(myBot)
                .start();
    }
}