package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        switch (camera) {
            case FORWARD:
                return builder
                        .lineToY(110 / 2.54)
                        .splineTo(new Vector2d(25.4 / 2.54, 85 / 2.54), -PI / 2)//forward
                        // .splineTo(new Vector2d(18 / 2.54, 90 / 2.54), -PI) //left
                        //.splineTo(new Vector2d(35 / 2.54, 90   / 2.54), 0) //right
                        //.strafeToLinearHeading(new Vector2d(18 / 2.54, 85 / 2.54), 0)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60 / 2.54, 150 / 2.54), 0)//universal
                        //.strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)//right
                        .strafeToLinearHeading(new Vector2d(123 / 2.54, 90 / 2.54), 0)//forward
                        //.strafeToLinearHeading(new Vector2d(123 / 2.54, 105 / 2.54), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(-80 / 2.54, 30 / 2.54), 0)
                        //врубили щётки
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145 / 2.54, 30 / 2.54), 0)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(122 / 2.54, 100 / 2.54), 0);
            break;
            case RIGHT:
                return builder
                        .lineToY(110 / 2.54)
                        //.splineTo(new Vector2d(25.4 / 2.54, 85 / 2.54), -PI / 2)//forward
                        // .splineTo(new Vector2d(18 / 2.54, 90 / 2.54), -PI) //left
                        .splineTo(new Vector2d(35 / 2.54, 90 / 2.54), 0) //right
                        .strafeToLinearHeading(new Vector2d(18 / 2.54, 85 / 2.54), 0)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60 / 2.54, 150 / 2.54), 0)//universal
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)//right
                        //.strafeToLinearHeading(new Vector2d(123 / 2.54, 90 / 2.54), 0)//forward
                        //.strafeToLinearHeading(new Vector2d(123 / 2.54, 105 / 2.54), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(-80 / 2.54, 30 / 2.54), 0)
                        //врубили щётки
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145 / 2.54, 30 / 2.54), 0)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(122 / 2.54, 100 / 2.54), 0);
            break;
            case LEFT:
                return builder
                        .lineToY(110 / 2.54)
                        .splineTo(new Vector2d(25.4 / 2.54, 85 / 2.54), -PI / 2)//forward
                        .splineTo(new Vector2d(18 / 2.54, 90 / 2.54), -PI) //left
                        //.splineTo(new Vector2d(35 / 2.54, 90   / 2.54), 0) //right
                        //.strafeToLinearHeading(new Vector2d(18 / 2.54, 85 / 2.54), 0)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60 / 2.54, 150 / 2.54), 0)//universal
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, 75 / 2.54), 0)//right
                        .strafeToLinearHeading(new Vector2d(123 / 2.54, 90 / 2.54), 0)//forward
                        //.strafeToLinearHeading(new Vector2d(123 / 2.54, 105 / 2.54), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(-80 / 2.54, 30 / 2.54), 0)
                        //врубили щёткb
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145 / 2.54, 30 / 2.54), 0)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, 30 / 2.54), 0)
                        .strafeToLinearHeading(new Vector2d(122 / 2.54, 100 / 2.54), 0);
            break;
        }
    }
}
