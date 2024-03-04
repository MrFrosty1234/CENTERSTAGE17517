package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.ftccommon.FtcAboutActivity;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder,
                                                                           StartRobotPosition startPos, CameraRobotPosition camera) {
        switch (startPos) {
            case BLUE_BACK: {
                switch (camera) {
                    case FORWARD: {
                        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(28)), PI / 2)
                                .strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(38)), PI / 2)
                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(30)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(30)), 0);

                        break;
                    }

                    case LEFT: {
                        builder.splineTo(new Vector2d(ToInch(-94), ToInch(80)), 0)
                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-149.9), ToInch(90)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(30)), 0);

                        break;
                    }

                    case RIGHT: {
                        builder.strafeToLinearHeading(new Vector2d(ToInch(-138), ToInch(50)), PI / 2)
                                .strafeToLinearHeading(new Vector2d(ToInch(-123), ToInch(50)), PI / 2)
                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .strafeToLinearHeading(new Vector2d(ToInch(-120), ToInch(30)), 0)
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(26)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(30)), 0)
                        ;

                        break;
                    }
                }

                if (camera == CameraRobotPosition.FORWARD || camera == CameraRobotPosition.RIGHT)
                    builder.splineToConstantHeading(new Vector2d(ToInch(60), ToInch(27)), 0);
                builder.splineToConstantHeading(new Vector2d(ToInch(30), ToInch(27)), 0)
                        .liftMiddle()
                ;
                switch (camera) {
                    case RIGHT:
                        builder.splineToConstantHeading(new Vector2d(ToInch(132.9), ToInch(61)), 0)
                                .turnTo(-0.15)
                                .setSpeed(0.8);//y подопрать по начальной позиции
                        break;
                    case LEFT:
                        builder.splineToConstantHeading(new Vector2d(ToInch(132.9), ToInch(87)), 0)
                                .turnTo(-0.15)
                                .setSpeed(0.8);//y подопрать по начальной позиции
                        break;
                    case FORWARD:
                        builder.splineToConstantHeading(new Vector2d(ToInch(132.9), ToInch(75)), 0)
                                .turnTo(-0.15)
                                .setSpeed(0.8);//y подопрать по начальной позиции
                        break;
                }//поднять лифт
                builder
                        .pixelDeGripp()
                        .setSpeed(1)
                        .strafeToLinearHeading(new Vector2d(ToInch(60), ToInch(34)), 0)
                        //опустить щётки
                        .brushOn()
                        .brushDown()
                        .strafeToLinearHeading(new Vector2d(ToInch(-140), ToInch(34)), 0)
                        //ограничить скорость и подъезд к стопке убрано

                        .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(36)), 0)
//хаваем
                        .waitPixel()
                        .splineToConstantHeading(new Vector2d(ToInch(30), ToInch(22)), 0)
                        .liftMiddle()
                        .splineToConstantHeading(new Vector2d(ToInch(130.3), ToInch(80)), 0)//не обязательно y подопрать по начальной позиции
                        .waitLift()
                        .pixelDeGripp()
                        .setSpeed(1);

                break;
            }

            case RED_BACK: {
                //                switch (camera) {
//                    case FORWARD: {
//                        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), -PI / 2)
//                                .strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(-35)), -PI / 2)
//
//                                .waitSeconds(0.2)
//                                .linePixelOpen()
//                                .brushOn()
//                                .brushDown()
//                                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(-30)), 0)
//                                .waitPixel()
//                                .setSpeed(1)
//                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), 0);
//
//                        break;
//                    }
//
//                    case RIGHT: {
//                        builder.splineTo(new Vector2d(ToInch(-94), ToInch(-80)), 0)
//                                .waitSeconds(0.2)
//                                .linePixelOpen()
//                                .brushOn()
//                                .brushDown()
//                                .strafeToLinearHeading(new Vector2d(ToInch(-149.9), ToInch(-90)), 0)
//                                .waitPixel()
//                                .setSpeed(1)
//                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), 0);
//
//                        break;
//                    }
//
//                    case LEFT: {
//                        builder.strafeToLinearHeading(new Vector2d(ToInch(-138), ToInch(-50)), -PI / 2)
//                                .strafeToLinearHeading(new Vector2d(ToInch(-123), ToInch(-50)), -PI / 2)
//                                .waitSeconds(0.2)
//                                .linePixelOpen()
//                                .strafeToLinearHeading(new Vector2d(ToInch(-120), ToInch(-30)), 0)
//                                .brushOn()
//                                .brushDown()
//                                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(-26)), 0)
//                                .waitPixel()
//                                .setSpeed(1)
//                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-26)), 0)
//                        ;
//
//                        break;
//                    }
//                }
//
//                if (camera == CameraRobotPosition.FORWARD || camera == CameraRobotPosition.LEFT)
//                    builder.splineToConstantHeading(new Vector2d(ToInch(60), ToInch(-10)), 0);
//                builder.splineToConstantHeading(new Vector2d(ToInch(30), ToInch(-10)), 0)
//                        .liftMiddle()
//                ;
//                switch (camera) {
//                    case LEFT:
//                        builder.setSpeed(0.5).splineToConstantHeading(new Vector2d(ToInch(140.9), ToInch(-64)), 0)
//                                ;//y подопрать по начальной позиции
//                        break;
//                    case RIGHT:
//                        builder.setSpeed(0.8).splineToConstantHeading(new Vector2d(ToInch(140.9), ToInch(-90)), 0)
//                                ;//y подопрать по начальной позиции
//                        break;
//                    case FORWARD:
//                        builder.setSpeed(0.8).splineToConstantHeading(new Vector2d(ToInch(139.9), ToInch(-78)), 0)
//                                ;//y подопрать по начальной позиции
//                        break;
//                }//поднять лифт
//                builder
//                        .pixelDeGripp()
//                        .setSpeed(1)
//                        .strafeToLinearHeading(new Vector2d(ToInch(60), ToInch(-29)), 0)
//                        //опустить щётки
//                        .brushOn()
//                        .brushDown()
//                        .splineToConstantHeading(new Vector2d(ToInch(-130), ToInch(-30)), 0)
//                        //ограничить скорость и подъезд к стопке убрано
//
//                        .strafeToLinearHeading(new Vector2d(ToInch(-145), ToInch(-40)), 0)
////хаваем
//                        .waitPixel()
//                        .splineToConstantHeading(new Vector2d(ToInch(30), ToInch(-22)), 0)
//                        .liftMiddle()
//                        .splineToConstantHeading(new Vector2d(ToInch(135.9), ToInch(-80)), 0)//не обязательно y подопрать по начальной позиции
//                        .waitLift()
//                        .pixelDeGripp()
//                        .setSpeed(1);

                switch (camera) {
                    case FORWARD: {
                        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), -PI / 2)
                                .strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(-35)), -PI / 2)

                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(-30)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), 0);

                        break;
                    }

                    case RIGHT: {
                        builder.splineTo(new Vector2d(ToInch(-94), ToInch(-80)), 0)
                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-149.9), ToInch(-90)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-28)), 0);

                        break;
                    }

                    case LEFT: {
                        builder.strafeToLinearHeading(new Vector2d(ToInch(-132), ToInch(-50)), -PI / 2)
                                .strafeToLinearHeading(new Vector2d(ToInch(-122), ToInch(-50)), -PI / 2)
                                .waitSeconds(0.2)
                                .linePixelOpen()
                                .strafeToLinearHeading(new Vector2d(ToInch(-120), ToInch(-30)), 0)
                                .brushOn()
                                .brushDown()
                                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(-26)), 0)
                                .waitPixel()
                                .setSpeed(1)
                                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(-26)), 0)
                        ;

                        break;
                    }
                }

                builder
                        .splineToConstantHeading(new Vector2d(ToInch(30), ToInch(-20)), 0)
                        .setSpeed(0.7)
                        .waitSeconds(0.7)
                        .setSpeed(1)
                        .strafeTo(new Vector2d(ToInch(128), ToInch(-47)));
                        /*.strafeToLinearHeading(new Vector2d(ToInch(60), ToInch(-32)), 0)
                        //опустить щётки
                        .brushOn()
                        .brushDown()
                        .splineToConstantHeading(new Vector2d(ToInch(-100), ToInch(-23)), 0)
                        //ограничить скорость и подъезд к стопке убрано
                        .splineToConstantHeading(new Vector2d(ToInch(-148), ToInch(-41)), 0)
//хаваем
                        .waitPixel()
                        .setSpeed(1)
                        .splineToConstantHeading(new Vector2d(ToInch(30), ToInch(-22)), 0)
                        .liftMiddle()
                        .splineToConstantHeading(new Vector2d(ToInch(135.9), ToInch(-80)), 0)//не обязательно y подопрать по начальной позиции
                        .waitLift()
                        .pixelDeGripp()
                        .setSpeed(1);*/

                break;
            }
        }

        return builder;
    }

    private static double ToInch(double val){
        return ToInch(val, false);
    }
    private static double ToInch(double value, boolean revers) {
        if(revers) value = -value;
        return value;
    }
}