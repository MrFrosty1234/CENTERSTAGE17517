package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
       /* switch (camera) {//синий передний
            case FORWARD:
                builder= builder
                        .lineToY(110 )
                        .splineTo(new Vector2d(25.4 , 85 ), -PI / 2)//forward
                        // .splineTo(new Vector2d(18 , 90 ), -PI) //left
                        //.splineTo(new Vector2d(35 , 90   ), 0) //right
                        //.strafeToLinearHeading(new Vector2d(18 , 85 ), 0)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60 , 150 ), 0)//universal
                        //.strafeToLinearHeading(new Vector2d(120 , 75 ), 0)//right
                        .strafeToLinearHeading(new Vector2d(123 , 90 ), 0)//forward
                        //.strafeToLinearHeading(new Vector2d(123 , 105 ), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70 , 30 ), 0)
                        .strafeToLinearHeading(new Vector2d(-80 , 30 ), 0)
                        //врубили щётки
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145 , 30 ), 0)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120 , 30 ), 0)
                        .strafeToLinearHeading(new Vector2d(122 , 100 ), 0);
            break;
            case RIGHT:
                builder= builder
                        .lineToY(110 )
                        //.splineTo(new Vector2d(25.4 , 85 ), -PI / 2)//forward
                        //.splineTo(new Vector2d(35 , 90 ), 0) //left
                        .splineTo(new Vector2d(18/2.54 , 85/2.54 ), PI) //right
                        .strafeToLinearHeading(new Vector2d(25.4, 85), PI)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60 , 150 ), 0)//universal
                        .strafeToLinearHeading(new Vector2d(120 , 75 ), 0)//right
                        //.strafeToLinearHeading(new Vector2d(123 , 90 ), 0)//forward
                        //.strafeToLinearHeading(new Vector2d(123 , 105 ), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70 , 30 ), 0)
                        .strafeToLinearHeading(new Vector2d(-80 , 30 ), 0)
                        //врубили щётки
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145 , 30 ), 0)
                        .waitSeconds(5)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120 , 30 ), 0)
                        .strafeToLinearHeading(new Vector2d(122 , 100 ), 0);
            break;
            case LEFT:
                builder = builder
                        .lineToY(110)
                        //.splineTo(new Vector2d(25.4, 85), -PI / 2)//forward
                        .splineTo(new Vector2d(25, 90), -PI) //left
                        .lineToX(30)//left
                        //.splineTo(new Vector2d(35, 90), 0) //right
                        //.strafeToLinearHeading(new Vector2d(18, 85), 0)//right
                        //пиксель на линию
                        .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                        //.strafeToLinearHeading(new Vector2d(120, 75), 0)//right
                        //.strafeToLinearHeading(new Vector2d(123, 90), 0)//forward
                        .strafeToLinearHeading(new Vector2d(123, 105), 0)//left
                        //тут ставим пиксель на задник
                        .strafeToLinearHeading(new Vector2d(70, 30), 0)
                        .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                        //врубили щётки
                        //начали опускать
                        .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                        //хаваем стопки
                        .strafeToLinearHeading(new Vector2d(120, 30), 0)
                        .strafeToLinearHeading(new Vector2d(122, 100), 0);
            break;

        }*/
       /* switch (camera) {//краный передний
            case RIGHT:
                builder = builder

                        //начало правого
                        .splineTo(new Vector2d(57 / 2.54, -100 / 2.54), PI / 2)//right
                        .strafeToLinearHeading(new Vector2d(57 / 2.54, -130 / 2.54), PI / 2)//right
                        .strafeToLinearHeading(new Vector2d(95 / 2.54, -130 / 2.54), PI / 2)//right
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, -110 / 2.54), 0)//right
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
                break;
            case FORWARD:
                builder = builder
                        //начало переднего
                        .splineTo(new Vector2d(40 / 2.54, -93 / 2.54), PI / 2)//forward
                        .strafeToLinearHeading(new Vector2d(40 / 2.54, -100 / 2.54), PI / 2)//forward
                        .strafeToLinearHeading(new Vector2d(100 / 2.54, -100 / 2.54), 0)//forward
                        .strafeToLinearHeading(new Vector2d(120 / 2.54, -90 / 2.54), 0)//forward

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
                break;
            case LEFT:
                builder = builder
                        //начало левого
                        .splineTo(new Vector2d(27 / 2.54, -80 / 2.54), PI)
                        .strafeToLinearHeading(new Vector2d(50 / 2.54, -80 / 2.54), PI)
                        .strafeToLinearHeading(new Vector2d(122 / 2.54, -73 / 2.54), 0)
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
                break;
        }*/
        builder = builder
                .lineToY(110)
                //.splineTo(new Vector2d(25.4, 85), -PI / 2)//forward
                .splineTo(new Vector2d(25, 90), -PI) //left
                .lineToX(30)//left
                //.splineTo(new Vector2d(35, 90), 0) //right
                //.strafeToLinearHeading(new Vector2d(18, 85), 0)//right
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                //.strafeToLinearHeading(new Vector2d(120, 75), 0)//right
                //.strafeToLinearHeading(new Vector2d(123, 90), 0)//forward
                .strafeToLinearHeading(new Vector2d(123, 105), 0)//left
                //тут ставим пиксель на задник
                .strafeToLinearHeading(new Vector2d(70, 30), 0)
                .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                //врубили щётки
                //начали опускать
                .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                //хаваем стопки
                .strafeToLinearHeading(new Vector2d(120, 30), 0)
                .strafeToLinearHeading(new Vector2d(122, 100), 0);
        return builder;
    }
}
