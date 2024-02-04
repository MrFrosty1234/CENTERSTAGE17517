package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        builder.brushOn();

        builder = builder
                .lineToY(110)
                //.splineTo(new Vector2d(25.4, 85), -PI / 2)//forward
                .splineTo(new Vector2d(35, 90), -PI) //left
                .lineToX(30)//left
                //.splineTo(new Vector2d(35, 90), 0) //right
                //.strafeToLinearHeading(new Vector2d(18, 85), 0)//right
                //пиксель на линию
                .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                //.strafeToLinearHeading(new Vector2d(120, 75), 0)//right
                //.strafeToLinearHeading(new Vector2d(123, 90), 0)//forward
                .strafeToLinearHeading(new Vector2d(123, 115), 0)//left
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
                .strafeToLinearHeading(new Vector2d(-90, 30), 0)
                .strafeToLinearHeading(new Vector2d(-145, 42), 0)
                //хаваем стопки
                .waitSeconds(4)
                .strafeToLinearHeading(new Vector2d(60, 30), 0)
                .waitSeconds(0.2)
                .strafeToLinearHeading(new Vector2d(107, 100), 0)
                .liftUp()
                .waitSeconds(1)
                .pixelDeGripp()
                .waitSeconds(1);

        /*builder = builder
                .splineTo(new Vector2d(27, -80), PI)
                .strafeToLinearHeading(new Vector2d(50, -80), PI)
                .strafeToLinearHeading(new Vector2d(122, -73), 0)
                //лифт
                .liftUp()
                .waitSeconds(1.5)
                .pixelDeGripp()
                .waitSeconds(1.5)
                .strafeToLinearHeading(new Vector2d(97, -30), 0)
                .strafeToLinearHeading(new Vector2d(-50, -30), 0)
                //запускаем щётки и опускаем обычные
                .brushDown()
                .brushOn()
                .strafeToLinearHeading(new Vector2d(-140, -27), 0)
                .waitSeconds(5)
                .strafeToLinearHeading(new Vector2d(60, -30), 0)
                .strafeToLinearHeading(new Vector2d(90, -80), 0)
                .waitSeconds(1.5   )
                .strafeToLinearHeading(new Vector2d(120, -80), 0)
                //ставим пиксель, ждём лифт
                .liftUp()
                .waitSeconds(1.5)
                .pixelDeGripp()
                .waitSeconds(1.5)
                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                .strafeToLinearHeading(new Vector2d(140, -30), 0);*/

        return builder;

        //builder.lineToY(-150).lineToX(80);

        /*switch (Bios.GetStartPosition()) {
            case BLUE_FORWAD: {
                switch (camera) {//синий передний
                    case FORWARD: {
                        builder = builder
                                .lineToY(110)
                                .splineTo(new Vector2d(25.4, 85), -PI / 2)//forward
                                // .splineTo(new Vector2d(18 , 90 ), -PI) //left
                                //.splineTo(new Vector2d(35 , 90   ), 0) //right
                                //.strafeToLinearHeading(new Vector2d(18 , 85 ), 0)//right
                                //пиксель на линию
                                .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                                //.strafeToLinearHeading(new Vector2d(120 , 75 ), 0)//right
                                .strafeToLinearHeading(new Vector2d(123, 90), 0)//forward
                                //.strafeToLinearHeading(new Vector2d(123 , 105 ), 0)//left
                                //тут ставим пиксель на задник
                                .strafeToLinearHeading(new Vector2d(70, 30), 0)
                                .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                                //врубили щётки
                                //начали опускать
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                                //хаваем стопки
                                .waitSeconds(10)
                                .strafeToLinearHeading(new Vector2d(120, 30), 0)
                                .strafeToLinearHeading(new Vector2d(122, 100), 0);
                        break;
                    }
                    case RIGHT: {
                        builder = builder
                                .lineToY(110)
                                //.splineTo(new Vector2d(25.4 , 85 ), -PI / 2)//forward
                                //.splineTo(new Vector2d(35 , 90 ), 0) //left
                                .splineTo(new Vector2d(18, 85), PI) //right
                                .strafeToLinearHeading(new Vector2d(25.4, 85), PI)//right
                                //пиксель на линию
                                .strafeToLinearHeading(new Vector2d(60, 150), 0)//universal
                                .strafeToLinearHeading(new Vector2d(120, 75), 0)//right
                                //.strafeToLinearHeading(new Vector2d(123 , 90 ), 0)//forward
                                //.strafeToLinearHeading(new Vector2d(123 , 105 ), 0)//left
                                //тут ставим пиксель на задник
                                .liftUp()
                                .waitSeconds(7)
                                .pixelDeGripp()
                                .waitSeconds(8)
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(70, 30), 0)
                                .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                                //врубили щётки
                                //начали опускать
                                .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                                .waitSeconds(5)
                                //хаваем стопки
                                .waitSeconds(10)
                                .strafeToLinearHeading(new Vector2d(120, 30), 0)
                                .strafeToLinearHeading(new Vector2d(122, 100), 0);
                        break;
                    }
                    case LEFT: {
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
                                .strafeToLinearHeading(new Vector2d(123, 115), 0)//left
                                //тут ставим пиксель на задник
                                .liftUp()
                                .waitSeconds(1.5)
                                .pixelDeGripp()
                                .waitSeconds(1.5)
                                .strafeToLinearHeading(new Vector2d(70, 30), 0)
                                .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                                //врубили щётки
                                //начали опускать
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-145, 30), 0)
                                //хаваем стопки
                                .waitSeconds(5)
                                .strafeToLinearHeading(new Vector2d(120, 35), 0)
                                .strafeToLinearHeading(new Vector2d(122, 100), 0);
                        break;
                    }
                }

                break;
            }
            case RED_FORWARD: {
                switch (camera) {//краный передний
                    case RIGHT: {
                        builder = builder

                                //начало правого
                                .splineTo(new Vector2d(57, -100), PI / 2)//right
                                .strafeToLinearHeading(new Vector2d(57, -130), PI / 2)//right
                                .strafeToLinearHeading(new Vector2d(95, -130), PI / 2)//right
                                .strafeToLinearHeading(new Vector2d(120, -110), 0)//right
                                .liftUp()
                                .waitSeconds(2)
                                .pixelDeGripp()
                                .waitSeconds(2)
                                .strafeToLinearHeading(new Vector2d(97, -35), 0)
                                .strafeToLinearHeading(new Vector2d(-50, -32), 0)
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-140, -32), 0)
                                .waitSeconds(10)
                                .strafeToLinearHeading(new Vector2d(100, -35), 0)
                                .strafeToLinearHeading(new Vector2d(120, -80), 0)
                                .liftUp()
                                .waitSeconds(2)
                                .pixelDeGripp()
                                .waitSeconds(2)
                                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                                .strafeToLinearHeading(new Vector2d(140, -30), 0);
                        //парковка
                        break;
                    }
                    case FORWARD: {
                        builder = builder
                                //начало переднего
                                .splineTo(new Vector2d(40, -93), PI / 2)//forward
                                .strafeToLinearHeading(new Vector2d(40, -100), PI / 2)//forward
                                .strafeToLinearHeading(new Vector2d(100, -100), 0)//forward
                                .strafeToLinearHeading(new Vector2d(120, -90), 0)//forward
                                //лифт
                                .liftUp()
                                .waitSeconds(7)
                                .pixelDeGripp()
                                .waitSeconds(8)
                                .strafeToLinearHeading(new Vector2d(97, -30), 0)
                                .strafeToLinearHeading(new Vector2d(-50, -30), 0)
                                //запускаем щётки и опускаем обычные
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-140, -27), 0)
                                //ждём пикселя
                                .waitSeconds(10)
                                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                                .strafeToLinearHeading(new Vector2d(120, -80), 0)
                                //ставим пиксель, ждём лифт
                                .liftUp()
                                .waitSeconds(7)
                                .pixelDeGripp()
                                .waitSeconds(8)
                                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                                .strafeToLinearHeading(new Vector2d(140, -30), 0);
                        //парковка
                        break;
                    }
                    case LEFT: {
                        builder = builder
                                //начало левого
                                .splineTo(new Vector2d(27, -80), PI)
                                .strafeToLinearHeading(new Vector2d(50, -80), PI)
                                .strafeToLinearHeading(new Vector2d(122, -73), 0)
                                //лифт
                                .liftUp()
                                .waitSeconds(7)
                                .pixelDeGripp()
                                .waitSeconds(8)
                                .strafeToLinearHeading(new Vector2d(97, -30), 0)
                                .strafeToLinearHeading(new Vector2d(-50, -30), 0)
                                //запускаем щётки и опускаем обычные
                                .brushDown()
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-140, -27), 0)
                                .waitSeconds(10)
                                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                                .strafeToLinearHeading(new Vector2d(120, -80), 0)
                                //ставим пиксель, ждём лифт
                                .liftUp()
                                .waitSeconds(7)
                                .pixelDeGripp()
                                .waitSeconds(8)
                                .strafeToLinearHeading(new Vector2d(100, -30), 0)
                                .strafeToLinearHeading(new Vector2d(140, -30), 0);
                        //парковка
                        break;
                    }
                }

                break;
            }
        }
        return builder;*/
    }
}
