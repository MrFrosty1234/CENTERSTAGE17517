package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        return builder
                .lineToY(110)
                //.splineTo(new Vector2d(25.4, 85), -PI / 2)//forward
                // .splineTo(new Vector2d(18, 90), -PI) //left
                .splineTo(new Vector2d(35, 90  ), 0) //right
                .strafeToLinearHeading(new Vector2d(18, 85), 0)
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                //.strafeToLinearHeading(new Vector2d(120, 75), 0)//right
                //.strafeToLinearHeading(new Vector2d(123, 90), 0)//forward
                .strafeToLinearHeading(new Vector2d(123, 105), 0)//left
                //тут ставим пиксель на задник
                .strafeToLinearHeading(new Vector2d(70, 30), 0)
                .strafeToLinearHeading(new Vector2d(-130, 30), 0)
                //врубили щётки
                .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                //хаваем стопки
                .strafeToLinearHeading(new Vector2d(120, 30), 0)
                .strafeToLinearHeading(new Vector2d(122, 100), 0);
        //return builder.turnTo(0);

        /*switch (camera){
            case FORWARD:
                break;
            case RIGHT:
                break;
            case LEFT:
                break;
        }*/
    }
}
