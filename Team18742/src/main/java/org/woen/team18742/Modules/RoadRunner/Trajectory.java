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
        switch (camera) {
            case FORWARD: {
                builder.strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(28)), PI / 2)
                        .strafeToLinearHeading(new Vector2d(ToInch(-97.9), ToInch(38)), PI / 2)

                        .waitSeconds(0.5)
                        .linePixelOpen()
                        .brushOn()
                        .brushDown()
                        .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(30)), 0)
                        .waitPixel()
                        .setSpeed(1)
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
                        .waitPixel()
                        .setSpeed(1)
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
                        .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(26)), 0)
                        .waitPixel()
                        .setSpeed(1)
                        .splineTo(new Vector2d(ToInch(-97.9), ToInch(30)), 0)
                        ;

                break;
            }
        }
        builder
                .strafeToLinearHeading(new Vector2d(ToInch(30), ToInch(27)), 0)
                .liftMiddle()
                .setSpeed(0.8);
        switch (camera) {
            case RIGHT:
            builder.splineToConstantHeading(new Vector2d(ToInch(131.9), ToInch(67)), 0);//y подопрать по начальной позиции
 break;
            case LEFT:
                builder.splineToConstantHeading(new Vector2d(ToInch(131.9), ToInch(81)), 0);//y подопрать по начальной позиции
                break;
            case FORWARD:
                builder.splineToConstantHeading(new Vector2d(ToInch(131.9), ToInch(75)), 0);//y подопрать по начальной позиции
                break;
        }//поднять лифт
        builder
                .waitLift()
                .pixelDeGripp()
                .setSpeed(1)
                .strafeToLinearHeading(new Vector2d(ToInch(60), ToInch(27)), 0)
                //опустить щётки
                .brushOn()
                .brushDown()
                .strafeToLinearHeading(new Vector2d(ToInch(-140), ToInch(30)), 0)
                //ограничить скорость и подъезд к стопке
                .setSpeed(0.8)
                .strafeToLinearHeading(new Vector2d(ToInch(-150), ToInch(36)), 0)
//хаваем
                .waitPixel()
                .setSpeed(1)
                //.waitSeconds(5)
                //.splineToConstantHeading(new Vector2d(ToInch(-97.9), ToInch(28)), 0)
                .strafeToLinearHeading(new Vector2d(ToInch(30), ToInch(27)), 0)
                .liftMiddle()
                .strafeToLinearHeading(new Vector2d(ToInch(127.9), ToInch(85)), 0)//не обязательно y подопрать по начальной позиции
                .waitLift()
                .pixelDeGripp()
                .setSpeed(1);
        //  .splineToLinearHeading(new Pose2d(new Vector2d(),));

        return builder;
    }

    private static double ToInch(double value) {
        return value;
    }
}