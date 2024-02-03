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
        double trackWidth = 35d;
        final double INCH_TO_CM = 1d / 2.54d;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(maxVel * INCH_TO_CM, maxAccel * INCH_TO_CM, Math.toRadians(180), Math.toRadians(180), trackWidth * INCH_TO_CM)
                .build();

        myBot.runAction(myBot.getDrive()
                .actionBuilder(new Pose2d(25.4 / 2.54, 154 / 2.54, -PI / 2))
                .lineToY(110 / 2.54)
                /*.splineTo(new Vector2d(25.4 / 2.54, 60 / 2.54), -PI/2)//forward
                // .splineTo(new Vector2d(18 / 2.54, 90 / 2.54), -PI) //left
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(90/2.54, 150/2.54), 0)
                .strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)
               // .lineToX(122 / 2.54)
                                //тут ставим пиксель на задник
                        //.splineTo(new Vector2d(0/2.54, 15/2.54), 0)
                        .strafeToLinearHeading(new Vector2d(25/2.54, 30/2.54), 0)
                .strafeToLinearHeading(new Vector2d(-130/2.54, 30/2.54), 0)
                //врубили щётки
                .strafeToLinearHeading(new Vector2d(-145/2.54, 30/2.54), 0)
                //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120/2.54, 30/2.54), 0)
                .strafeToLinearHeading(new Vector2d(122/2.54, 100/2.54), 0)*/
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}