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
        /*builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(88)), -PI / 2);
        builder.linePixelOpen();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(93)), 0);
        builder.brushOn();
        builder.brushDown();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-149.9), ToInch(90)), 0);
        builder.waitPixel();
        builder.strafeToLinearHeading(new Vector2d(ToInch(70.9), ToInch(89)), 0);
        builder.strafeToLinearHeading(new Vector2d(ToInch(125.9), ToInch(80)), 0);
        builder.liftMiddle();
        builder.waitSeconds(1.6);
        builder.pixelDeGripp();
        //builder.waitSeconds(0.7);
        builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(108 )), 0);
        builder.brushOn();
        builder.brushDown();
        builder.strafeToLinearHeading(new Vector2d(ToInch(-146), ToInch(94)), 0);
        builder.waitPixel();
        builder.strafeToLinearHeading(new Vector2d(ToInch(70.9), ToInch(92)), 0);
        builder.strafeToLinearHeading(new Vector2d(ToInch(124.9), ToInch(80)), 0);
        builder.liftMiddle();
        builder.waitSeconds(1.6);
        builder.pixelDeGripp();*/

        switch (camera) {
            case FORWARD: {
                builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(38)), PI / 2)
                        .waitSeconds(0.5)
                        .linePixelOpen()
                        .brushOn()
                        .brushDown()
                        .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(30)), 0)
                        .waitSeconds(5)
                        .splineTo(new Vector2d(ToInch(-97.9), ToInch(30)), 0);

                break;
            }

            case LEFT: {
                builder.splineTo(new Vector2d(ToInch(-90), ToInch(80)), 0)
                        .waitSeconds(0.5)
                        .linePixelOpen()
                        .brushOn()
                        .brushDown()
                        .strafeToLinearHeading(new Vector2d(ToInch(-149.9), ToInch(90)), 0)
                        .waitSeconds(5)
                        .strafeTo(new Vector2d(ToInch(-97.9), ToInch(30)));

                break;
            }

            case RIGHT: {
                builder.strafeToLinearHeading(new Vector2d(ToInch(-138), ToInch(50)), PI / 2)
                        .strafeToLinearHeading(new Vector2d(ToInch(-123), ToInch(50)), PI / 2)
                        .waitSeconds(0.5)
                        .linePixelOpen()
                        .strafeToLinearHeading(new Vector2d(ToInch(-120), ToInch(30)), 0)
                        .setSpeed(0.4)
                        .brushOn()
                        .brushDown()
                        .strafeToLinearHeading(new Vector2d(ToInch(-153), ToInch(26)), 0)
                        .waitPixel()
                        .splineTo(new Vector2d(ToInch(-97.9), ToInch(30)), 0)
                        .setSpeed(1);

                break;
            }
        }
        builder
                .strafeToLinearHeading(new Vector2d(ToInch(110), ToInch(23)), 0)
                .strafeToLinearHeading(new Vector2d(ToInch(90), ToInch(30)), 0)
                .setSpeed(0.4);
                builder.splineToConstantHeading(new Vector2d(ToInch(129.9), ToInch(86)), 0);//y подопрать по начальной позиции
                //поднять лифт
                builder.liftMiddle()
                .waitSeconds(1.5)
                .pixelDeGripp()
                        .setSpeed(1)
                .strafeToLinearHeading(new Vector2d(ToInch(60), ToInch(27)), 0)
                //опустить щётки
                .brushOn()
                .brushDown()
                .strafeToLinearHeading(new Vector2d(ToInch(-140), ToInch(26)), 0)
                //ограничить скорость и подъезд к стопке
                        .setSpeed(0.4)
                .strafeToLinearHeading(new Vector2d(ToInch(-152), ToInch(29)), 0)
//хаваем
                        .waitPixel()
                        .setSpeed(1)
                //.waitSeconds(5)
                .splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(28)), 0)
                .strafeToLinearHeading(new Vector2d(ToInch(90), ToInch(27)), 0)
                .splineToConstantHeading(new Vector2d(ToInch(127.9), ToInch(85)), 0)//не обязательно y подопрать по начальной позиции
                        .liftMiddle()
                        .waitSeconds(1.5)
                        .pixelDeGripp()
                        .setSpeed(1);
        //  .splineToLinearHeading(new Pose2d(new Vector2d(),));

        return builder;
    }

    private static double ToInch(double value) {
        return value;
    }
}