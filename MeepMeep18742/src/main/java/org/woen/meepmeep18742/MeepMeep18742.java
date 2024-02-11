package org.woen.meepmeep18742;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeep18742 {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        double maxVel = 150d;
        double maxAccel = 150d;
        double maxAngVel = Math.toRadians(180d);
        double maxAngAccel = Math.toRadians(180d);
        double trackWidth = 15.7 / 2.54 * 2 + 2;
        final double INCH_TO_CM = 1d / 2.54d;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(maxVel * INCH_TO_CM, maxAccel * INCH_TO_CM, Math.toRadians(180), Math.toRadians(180), trackWidth * INCH_TO_CM)
                .build();

        myBot.runAction(myBot.getDrive()
                .actionBuilder(new Pose2d(-93.7/2.54, 156.4/2.54, -PI/2))
                .strafeToLinearHeading(new Vector2d(-70/2.54, 120/2.54), -PI / 2)
                .strafeToLinearHeading(new Vector2d(-61/2.54, 85/2.54), -PI / 2 / 2 / 2)
                //.brushOn()
                //.strafeToLinearHeading(new Vector2d(-95, 100), 0)
                .strafeToLinearHeading(new Vector2d(-98/2.54, 90/2.54), 0)
                //.waitPixel()
                .strafeToLinearHeading(new Vector2d(-90/2.54, 90/2.54), 0)
                .strafeToLinearHeading(new Vector2d(-90/2.54, 25/2.54), 0)
                .strafeToLinearHeading(new Vector2d(90/2.54, 25/2.54), 0)
                //.liftUp()
                .strafeToLinearHeading(new Vector2d(123/2.54, 85/2.54), 0)
                .waitSeconds(0.4)
                //.waitLift()
                //.pixelDeGripp()
                .waitSeconds(1)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}