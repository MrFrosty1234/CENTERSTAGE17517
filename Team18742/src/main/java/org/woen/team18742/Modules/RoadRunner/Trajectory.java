package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        return builder
                .lineToY(110 / 2.54)
                .splineTo(new Vector2d(18 / 2.54, 90 / 2.54), -PI)
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(90 / 2.54, 150 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)
                // .lineToX(122 / 2.54)
                .liftUp()
                .waitLift()
                .pixelDegrip()

                //тут ставим пиксель на задник
                //.splineTo(new Vector2d(0/2.54, 15/2.54), 0)
                .strafeToLinearHeading(new Vector2d(25 / 2.54, 30 / 2.54), 0)
                .brushOn(1.0)//врубили щётки
                .strafeToLinearHeading(new Vector2d(-130 / 2.54, 30 / 2.54), 0)

                .strafeToLinearHeading(new Vector2d(-145 / 2.54, 30 / 2.54), 0)
                //хаваем стопки
                .strafeToLinearHeading(new Vector2d(120 / 2.54, 30 / 2.54), 0)
                .strafeToLinearHeading(new Vector2d(122 / 2.54, 100 / 2.54), 0);
        //return builder.turnTo(0);
    }
}
