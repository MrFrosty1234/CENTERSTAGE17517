package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.ftccommon.FtcAboutActivity;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder,
                                                                           StartRobotPosition startPos, CameraRobotPosition camera) {
        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(90)), -PI / 2);
        builder.linePixelOpen();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(93)), 0);
        builder.brushOn();
        builder.brushDown();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-153.9), ToInch(90)), 0);
        builder.waitPixel();
        builder.strafeToLinearHeading(new Vector2d(ToInch(117.9), ToInch(90)), 0);
        builder.liftMiddle();
        builder.waitSeconds(2);
        builder.pixelDeGripp();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(93)), 0);
        builder.brushOn();
        builder.brushDown();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-153.9), ToInch(86)), 0);
        builder.waitPixel();
        builder.strafeToLinearHeading(new Vector2d(ToInch(117.9), ToInch(92)), 0);
        builder.liftMiddle();
        builder.waitSeconds(2);
        builder.pixelDeGripp();

//        switch (startPos) {
//            case BLUE_BACK: {
//                switch (camera) {
//                    case RIGHT:
//                        builder.strafeToLinearHeading(new Vector2d(-82 ,118), -2)
//                                .linePixelOpen()
//                                .strafeToLinearHeading(new Vector2d(-70, 135), -PI / 2)
//                                //.strafeToLinearHeading(new Vector2d(-116, 105), -PI / 2)
//
//                        /*//сюда и до парковки
//                        .strafeToLinearHeading(new Vector2d(-116, 50), -PI / 2)
//                               // .turnTo(0)
//                                .strafeToLinearHeading(new Vector2d(126, 27), -PI / 2)*/
//
//
//
//
//
//
//                        ;
//                        ;break;
//                    case LEFT:
//                        builder//.brushOn().brushDown(1)
//                                .strafeToLinearHeading(new Vector2d(-58, 110), -PI/2)
//                                .strafeToLinearHeading(new Vector2d(-59, 88), 0)
//                                .linePixelOpen()
//                                .waitSeconds(1)
//                                .strafeToLinearHeading(new Vector2d(-84, 88), 0)
//
//                                /*.strafeToLinearHeading(new Vector2d(-65, 135), -PI / 2)
//                                .strafeToLinearHeading(new Vector2d(-111, 105), -PI / 2)
//
//
//
//
//
//                        //тут начало обрубка кода для теста
//                                // .strafeToLinearHeading(new Vector2d(-95, 100), 0)
//                                /*.strafeToLinearHeading(new Vector2d(-96, 92), 0)
//                                .waitPixel()
//                                .strafeToLinearHeading(new Vector2d(-90, 90), 0)
//                                .strafeToLinearHeading(new Vector2d(-90, 25), 0)
//                                .strafeToLinearHeading(new Vector2d(90, 30), 0)
//                                .liftMiddle()
//                                .strafeToLinearHeading(new Vector2d(145 , 80), 0)
//                                .waitSeconds(0.1)
//                                .waitLift()
//                                .pixelDeGripp()
//                                .waitSeconds(1)
//                                .brushOn()
//                                .strafeToLinearHeading(new Vector2d(120, 32), 0)
//                                .strafeToLinearHeading(new Vector2d(140, 25), 0)*/;
//                        break;
//                    case FORWARD:
////forward
//                    builder
//                            .strafeToLinearHeading(new Vector2d(-72, 105), -PI / 2)
//                            .linePixelOpen()
//                            .strafeToLinearHeading(new Vector2d(-72, 115), -PI / 2)
//                           /* .strafeToLinearHeading(new Vector2d(-76, 135), -PI / 2)
//                            .strafeToLinearHeading(new Vector2d(-105, 90), -PI / 2)
//
//
////новый код
//
//                    //начало обрубка для теста
//                            /*.brushOn()
//                            .strafeToLinearHeading(new Vector2d(-98, 96), 0)
//                            .strafeToLinearHeading(new Vector2d(-118, 89), 0)
//                            .waitPixel()
//                            .strafeToLinearHeading(new Vector2d(60, 97), 0)
//                            .waitSeconds(0.7)
//                            .liftMiddle(0.5)
//                            .strafeToLinearHeading(new Vector2d(110, 80), 0)
//                            .waitSeconds(0.3)
//                            .strafeToLinearHeading(new Vector2d(124, 80), 0)
//                            .waitSeconds(0.5)
//                            //.waitSeconds(0.5)
//                            .waitLift()
//                            .waitSeconds(1)
//                            .pixelDeGripp()
//                            .waitSeconds(2)
//                            .strafeToLinearHeading(new Vector2d(120, 28), 0)
//                            .strafeToLinearHeading(new Vector2d(150, 28), 0)*/;
//                break;
//                }
//                break;
//            }
//
//            case RED_BACK: {
//                switch (camera) {
//                    case RIGHT:
//                        builder//.brushOn()
//                        //.brushDown(4)
//                                //.waitSeconds(5)
//                                .strafeToLinearHeading(new Vector2d(-58, -132), PI / 2)
//                                .strafeToLinearHeading(new Vector2d(-61, -100),PI / 2 / 2 / 2)
//                                //тут ноый код
//                                .strafeToLinearHeading(new Vector2d(-70, -135), PI/2)
//                                .waitSeconds(0.7)
//                                .strafeToLinearHeading(new Vector2d(-95, -120), PI/2)
//
//                        //прода старого
//                                /*.brushOn()
//                                //.strafeToLinearHeading(new Vector2d(-95, 100), 0)
//                                .strafeToLinearHeading(new Vector2d(-96, -92), 0)
//                                .waitPixel()
//                                .strafeToLinearHeading(new Vector2d(-90, -90), 0)
//                                .strafeToLinearHeading(new Vector2d(-90, -25), 0)
//                                .strafeToLinearHeading(new Vector2d(90, -30), 0)
//                                .liftMiddle()
//                                .strafeToLinearHeading(new Vector2d(143, -100), 0)
//                                .waitSeconds(0.1)
//                                .waitLift()
//                                .pixelDeGripp()
//                                .waitSeconds(1)
//                                .brushOn()
//                                .strafeToLinearHeading(new Vector2d(120, -32), 0)
//                                .strafeToLinearHeading(new Vector2d(150, -32), 0)*/;
//                        break;
//                    case LEFT:
//                        builder
//                                //.strafeToLinearHeading(new Vector2d(-78, -140), PI/2)//прямо
//                                .strafeToLinearHeading(new Vector2d(-89, -130), 1.9)
//                                //тут ноый код
//                                .strafeToLinearHeading(new Vector2d(-74, -135), PI/2)
//                                .waitSeconds(0.7)
//                                .strafeToLinearHeading(new Vector2d(-95, -120), PI/2)
//                        //прода старого
//                                ;
//                        break;
//                    case FORWARD:
//                        //forward
//                        builder//.brushDown(4)
//                                //.waitSeconds(3)
//                                .strafeToLinearHeading(new Vector2d(-74, -105), PI / 2)
//                        //тут ноый код
//                                .strafeToLinearHeading(new Vector2d(-74, -135), PI/2)
//                                .waitSeconds(0.7)
//                                .strafeToLinearHeading(new Vector2d(-95, -120), PI/2)
//                        //прода старого
//                                //отпустить пиксель и щётки врубили и опустили
//                                /*.brushOn()
//                                .strafeToLinearHeading(new Vector2d(-98, -96), 0)
//                                .strafeToLinearHeading(new Vector2d(-118, -89), 0)
//                                .waitPixel()
//                                .strafeToLinearHeading(new Vector2d(60, -64), 0)
//                                .waitSeconds(0.7)
//                                .liftMiddle(0.5)
//                                .strafeToLinearHeading(new Vector2d(120, -80), 0)
//                                .waitSeconds(0.5)
//                                //.waitSeconds(0.5)
//                                .waitLift()
//                                .waitSeconds(0.5)
//                                .pixelDeGripp()
//                                .waitSeconds(2)
//                                /*.strafeToLinearHeading(new Vector2d(-35, -96), 0)
//                                .waitSeconds(0.2)
//                                .brushOn()
//                                .brushDown()
//                                .strafeToLinearHeading(new Vector2d(-103, -78), 0)
//                                .waitPixel()
//                                .strafeToLinearHeading(new Vector2d(20, -78), 0)
//                                .waitSeconds(0.7)
//                                .liftMiddle(0.5)
//                                .strafeToLinearHeading(new Vector2d(103, -78), 0)
//                                //.waitSeconds(0.5)
//                                .waitLift()
//                                .pixelDeGripp()
//                                .waitSeconds(2)
//                                .strafeToLinearHeading(new Vector2d(120, -32), 0)
//                                .strafeToLinearHeading(new Vector2d(150, -32), 0)*/;
//
//                        /*.waitPixel()
//                        .liftMiddle(4)
//                        .strafeToLinearHeading(new Vector2d(100, 89), 0)
//                        .waitLift()
//                        .brushOn(2)
//                        .brushDown(3)
//                        .pixelDeGripp()
//                        .waitSeconds(2)
//                        .strafeToLinearHeading(new Vector2d(120, 30), 0)
//
//                        .strafeToLinearHeading(new Vector2d(150, 30), 0)*/
//                        break;
//                }
//                break;
//            }
//
//            case BLUE_FORWAD: {
//                builder.strafeToLinearHeading(new Vector2d(130, 90), 0)//left
//                        //тут ставим пиксель на задник
//                        .liftMiddle()
//                        .waitSeconds(1.4)
//                        .pixelDeGripp()
//                        .waitSeconds(1)
//                        .strafeToLinearHeading(new Vector2d(70, 30), 0)
//                        .strafeToLinearHeading(new Vector2d(-80, 30), 0)
//                        //врубили щётки
//                        //начали опускать
//                        .brushDown()
//                        .brushOn()
//                        .strafeToLinearHeading(new Vector2d(-145, 60), 0)
//                        //.strafeToLinearHeading(new Vector2d(-145, -40), 0)
//                        //хаваем стопки
//                        .waitSeconds(5)
//                        .strafeToLinearHeading(new Vector2d(60, 30), 0)
//                        .waitSeconds(0.2)
//                        .strafeToLinearHeading(new Vector2d(130, 100), 0)
//                        .liftMiddle()
//                        .waitSeconds(1.4)
//                        .pixelDeGripp()
//                        .waitSeconds(1);
//                break;
//            }
//
//            case RED_FORWARD: {
//                builder.strafeToLinearHeading(new Vector2d(90, -90), PI/2)//left
//
//                ;break;
//
//            }
//        }

        return builder;
    }

    private static double ToInch(double value) {
        return value;
    }
}
//blue back copy
 /*switch (camera) {
                    case LEFT:
                        builder.brushDown(1)
                                .strafeToLinearHeading(new Vector2d(-58, 123), -PI / 2)
                                .strafeToLinearHeading(new Vector2d(-61, 85), -PI / 2 / 2 / 2)
                                .brushOn()
                                //.strafeToLinearHeading(new Vector2d(-95, 100), 0)
                                .strafeToLinearHeading(new Vector2d(-96, 92), 0)
                                .waitPixel()
                                .strafeToLinearHeading(new Vector2d(-90, 90), 0)
                                .strafeToLinearHeading(new Vector2d(-90, 32), 0)
                                .strafeToLinearHeading(new Vector2d(90, 30), 0)
                                .liftMiddle()
                                .strafeToLinearHeading(new Vector2d(137, 106), 0)
                                .waitSeconds(0.1)
                                .waitLift()
                                .pixelDeGripp()
                                .waitSeconds(1)
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(120, 32), 0)
                                .strafeToLinearHeading(new Vector2d(150, 32), 0);

                            /*.strafeToLinearHeading(new Vector2d(90, -32), 0)
                            .brushDown()
                            .strafeToLinearHeading(new Vector2d(-40, -32), 0)

                            .waitSeconds(0.4)
                            .strafeToLinearHeading(new Vector2d(-94, -32), 0)
                            //.strafeToLinearHeading(new Vector2d(-90, -90), 0)
                            //.strafeToLinearHeading(new Vector2d(-93, -92), 0)
                            .waitPixel()
                            .strafeToLinearHeading(new Vector2d(-90, -90), 0)
                            .strafeToLinearHeading(new Vector2d(-90, -32), 0)
                            .strafeToLinearHeading(new Vector2d(90, -32), 0)
                            .liftMiddle()
                            .strafeToLinearHeading(new Vector2d(153, -82), 0)
                            .waitSeconds(0.1)
                            .waitLift()
                            .pixelDeGripp()
                            .waitSeconds(1);//*/

                  /*      break;
                    case RIGHT:
                        builder.brushDown(1)
                                .strafeToLinearHeading(new Vector2d(-58, 123), -PI / 2)
                                .strafeToLinearHeading(new Vector2d(-61, 85), -PI / 2 / 2 / 2)
                                .brushOn()
                                //.strafeToLinearHeading(new Vector2d(-95, 100), 0)
                                .strafeToLinearHeading(new Vector2d(-96, 92), 0)
                                .waitPixel()
                                .strafeToLinearHeading(new Vector2d(-90, 90), 0)
                                .strafeToLinearHeading(new Vector2d(-90, 32), 0)
                                .strafeToLinearHeading(new Vector2d(90, 30), 0)
                                .liftMiddle()
                                .strafeToLinearHeading(new Vector2d(137, 106), 0)
                                .waitSeconds(0.1)
                                .waitLift()
                                .pixelDeGripp()
                                .waitSeconds(1)
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(120, 32), 0)
                                .strafeToLinearHeading(new Vector2d(150, 32), 0);

                            /*.strafeToLinearHeading(new Vector2d(90, -32), 0)
                            .brushDown()
                            .strafeToLinearHeading(new Vector2d(-40, -32), 0)

                            .waitSeconds(0.4)
                            .strafeToLinearHeading(new Vector2d(-94, -32), 0)
                            //.strafeToLinearHeading(new Vector2d(-90, -90), 0)
                            //.strafeToLinearHeading(new Vector2d(-93, -92), 0)
                            .waitPixel()
                            .strafeToLinearHeading(new Vector2d(-90, -90), 0)
                            .strafeToLinearHeading(new Vector2d(-90, -32), 0)
                            .strafeToLinearHeading(new Vector2d(90, -32), 0)
                            .liftMiddle()
                            .strafeToLinearHeading(new Vector2d(153, -82), 0)
                            .waitSeconds(0.1)
                            .waitLift()
                            .pixelDeGripp()
                            .waitSeconds(1);//*/
                  /*      break;
                    case FORWARD:
                        //forward
                        builder.brushDown(1)
                                .waitSeconds(5)
                                .strafeToLinearHeading(new Vector2d(-68, 109), PI / 2)
                                //отпустить пиксель и щётки врубили и опустили
                                .brushOn()
                                .strafeToLinearHeading(new Vector2d(-98, 96), 0)
                                .strafeToLinearHeading(new Vector2d(-118, 89), 0)
                                .waitPixel()
                                .strafeToLinearHeading(new Vector2d(60, 64), 0)
                                .waitSeconds(0.7)
                                .liftMiddle(0.5)
                                .strafeToLinearHeading(new Vector2d(114, 80), 0)
                                .waitSeconds(0.5)
                                //.waitSeconds(0.5)
                                .waitLift()
                                .waitSeconds(0.5)
                                .pixelDeGripp()
                                .waitSeconds(2)
                                /*.strafeToLinearHeading(new Vector2d(-35, -96), 0)
                                .waitSeconds(0.2)
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(-103, -78), 0)
                                .waitPixel()
                                .strafeToLinearHeading(new Vector2d(20, -78), 0)
                                .waitSeconds(0.7)
                                .liftMiddle(0.5)
                                .strafeToLinearHeading(new Vector2d(103, -78), 0)
                                //.waitSeconds(0.5)
                                .waitLift()
                                .pixelDeGripp()
                                .waitSeconds(2)*/
                      /*          .strafeToLinearHeading(new Vector2d(120, 32), 0)
                                .strafeToLinearHeading(new Vector2d(150, 32), 0);

                        /*.waitPixel()
                        .liftMiddle(4)
                        .strafeToLinearHeading(new Vector2d(100, 89), 0)
                        .waitLift()
                        .brushOn(2)
                        .brushDown(3)
                        .pixelDeGripp()
                        .waitSeconds(2)
                        .strafeToLinearHeading(new Vector2d(120, 30), 0)

                        .strafeToLinearHeading(new Vector2d(150, 30), 0)*/
                      /*  break;
                }
                break;*/
//forward