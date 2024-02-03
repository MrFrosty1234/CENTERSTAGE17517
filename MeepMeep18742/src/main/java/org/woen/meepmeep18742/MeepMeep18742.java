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
                .actionBuilder(new Pose2d(37.4 / 2.54, 154 / 2.54, -PI / 2))
                .lineToY(110 / 2.54)
                //.splineTo(new Vector2d(25.4 , 85 ), -PI / 2)//forward
                // .splineTo(new Vector2d(18 , 90 ), -PI) //left
                .splineTo(new Vector2d(35 / 2.54, 90 / 2.54), 0) //right
                .strafeToLinearHeading(new Vector2d(18 / 2.54, 85 / 2.54), 0)//right
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(60 / 2.54, 150 / 2.54), 0)//universal
                .strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)//right
                //.strafeToLinearHeading(new Vector2d(123 , 90 ), 0)//forward
                //.strafeToLinearHeading(new Vector2d(123 , 105 ), 0)//left
                //тут ставим пиксель на задник
                .strafeToLinearHeading(new Vector2d(70 / 2.54, 30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(-80 / 2.54, 30 / 2.54), 0)
                //врубили щётки
                //начали опускать
                .strafeToLinearHeading(new Vector2d(-145 / 2.54, 30 / 2.54), 0)
                .waitSeconds(5)
                //хаваем стопки
                .strafeToLinearHeading(new Vector2d(120 / 2.54, 30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(122 / 2.54, 100 / 2.54), 0)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}