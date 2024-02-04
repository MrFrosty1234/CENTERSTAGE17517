package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        builder.brushOn();

        switch (Bios.GetStartPosition()) {
            case BLUE_FORWAD: {
                builder.strafeToLinearHeading(new Vector2d(130, 90), 0)//left
                        //тут ставим пиксель на задник
                        .liftUp()
                        .waitSeconds(1)
                        .pixelDeGripp()
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(70, 30), 0)
                        .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                        //врубили щётки
                        //начали опускать
                        .brushDown()
                        .brushOn()
                        .strafeToLinearHeading(new Vector2d(-90, 35), 0)
                        .strafeToLinearHeading(new Vector2d(-145, 45), 0)
                        //хаваем стопки
                        .waitSeconds(5)
                        .strafeToLinearHeading(new Vector2d(60, 30), 0)
                        .waitSeconds(0.2)
                        .strafeToLinearHeading(new Vector2d(130, 100), 0)
                        .liftUp()
                        .waitSeconds(1)
                        .pixelDeGripp()
                        .waitSeconds(1);

                break;
            }

            case RED_FORWARD: {
                builder.strafeToLinearHeading(new Vector2d(130, -90), 0)//left
                        //тут ставим пиксель на задник
                        .liftUp()
                        .waitSeconds(1)
                        .pixelDeGripp()
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(70, -30), 0)
                        .strafeToLinearHeading(new Vector2d(-80, -30), 0)
                        //врубили щётки
                        //начали опускать
                        .brushDown()
                        .brushOn()
                        .strafeToLinearHeading(new Vector2d(-90, -35), 0)
                        .strafeToLinearHeading(new Vector2d(-145, -45), 0)
                        //хаваем стопки
                        .waitSeconds(5)
                        .strafeToLinearHeading(new Vector2d(60, -30), 0)
                        .waitSeconds(0.2)
                        .strafeToLinearHeading(new Vector2d(130, -100), 0)
                        .liftUp()
                        .waitSeconds(1)
                        .pixelDeGripp()
                        .waitSeconds(1);

                break;
            }
        }

        return builder;
    }
}
