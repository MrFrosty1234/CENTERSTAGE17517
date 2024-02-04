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
                .actionBuilder(new Pose2d(52 / 2.54, -185.7 / 2.54, PI / 2))
                /*
                //начало правого
                .splineTo(new Vector2d(57 / 2.54, -100 / 2.54), PI / 2)//right
                .strafeToLinearHeading(new Vector2d(57 / 2.54, -130 / 2.54), PI / 2)//right
                .strafeToLinearHeading(new Vector2d(95 / 2.54, -130 / 2.54), PI / 2)//right
                .strafeToLinearHeading(new Vector2d(120 / 2.54, -110 / 2.54), 0)//right
                */
                /*
                //начало переднего
                .splineTo(new Vector2d(40/2.54, -93/2.54), PI/2)//forward
                .strafeToLinearHeading(new Vector2d(40/2.54, -100/2.54), PI/2)//forward
                .strafeToLinearHeading(new Vector2d(100/2.54, -100/2.54), 0)//forward
                .strafeToLinearHeading(new Vector2d(120/2.54, -90/2.54), 0)//forward
                */
                /*
                //начало левого
                .splineTo(new Vector2d(27/2.54, -80/2.54), PI)
                .strafeToLinearHeading(new Vector2d(50/2.54, -80/2.54), PI)
                .strafeToLinearHeading(new Vector2d(122/2.54, -73/2.54), 0)

                 */
                //лифт
                .strafeToLinearHeading(new Vector2d(97 / 2.54, -30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(-50 / 2.54, -30 / 2.54), 0)
                //запускаем щётки и опускаем обычные
                .strafeToLinearHeading(new Vector2d(-140 / 2.54, -27 / 2.54), 0)
                //ждём пикселя
                .strafeToLinearHeading(new Vector2d(100 / 2.54, -30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(120 / 2.54, -80 / 2.54), 0)
                //ставим пиксель, ждём лифт
                .strafeToLinearHeading(new Vector2d(100 / 2.54, -30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(140 / 2.54, -30 / 2.54), 0)
                //парковка


                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}