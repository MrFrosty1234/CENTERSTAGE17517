package org.woen.meepmeep17517;

import static java.lang.Math.PI;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeep17517 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
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
                .actionBuilder(new Pose2d(-150*INCH_TO_CM, 150*INCH_TO_CM, 0))
                                .splineToLinearHeading(new Pose2d(50*INCH_TO_CM,150*INCH_TO_CM,toRadians(45)),toRadians(90))
                //.splineTo(new Vector2d(100 * INCH_TO_CM, 30 * INCH_TO_CM), 0)
                //.lineToX(30 * INCH_TO_CM)
                //.turn(PI / 2)
                //.lineToY(-30 * INCH_TO_CM)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}