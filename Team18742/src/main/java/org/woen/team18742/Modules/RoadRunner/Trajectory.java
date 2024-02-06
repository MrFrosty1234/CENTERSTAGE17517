package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder,
                                                                           StartRobotPosition startPos, CameraRobotPosition camera) {
        builder.brushOn();

        switch (startPos) {
            case BLUE_BACK: {
                builder.brushDown(1)
                        .strafeToLinearHeading(new Vector2d(-95, 95), -PI / 2)
                        //отпустить пиксель и щётки врубили и опустили
                        .brushOn()
                        .strafeToLinearHeading(new Vector2d(-95, 100), 0)
                        .strafeToLinearHeading(new Vector2d(-110, 95), 0)
                        .waitPixel()
                        .liftUp(4)
                        .strafeToLinearHeading(new Vector2d(100, 84), 0)
                        .waitLift()
                        .brushOn(2)
                        .brushDown(3)
                        .pixelDeGripp()
                        .waitSeconds(2)
                        .strafeToLinearHeading(new Vector2d(-94, 100), 0)
                        .waitPixel()
                        .liftUp(4)
                        .strafeToLinearHeading(new Vector2d(100, 89), 0)
                        .waitLift()
                        .pixelDeGripp()
                        .waitSeconds(2)
                        .strafeToLinearHeading(new Vector2d(120, 30), 0)
                        .strafeToLinearHeading(new Vector2d(150, 30), 0);

                break;
            }

            case BLUE_FORWAD: {
                builder.strafeToLinearHeading(new Vector2d(130, 90), 0)//left
                        //тут ставим пиксель на задник
                        .liftUp()
                        .waitSeconds(1.4)
                        .pixelDeGripp()
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(70, 30), 0)
                        .strafeToLinearHeading(new Vector2d(-80, 30), 0)
                        //врубили щётки
                        //начали опускать
                        .brushDown()
                        .brushOn()
                        .strafeToLinearHeading(new Vector2d(-145, 60), 0)
                        //.strafeToLinearHeading(new Vector2d(-145, -40), 0)
                        //хаваем стопки
                        .waitSeconds(5)
                        .strafeToLinearHeading(new Vector2d(60, 30), 0)
                        .waitSeconds(0.2)
                        .strafeToLinearHeading(new Vector2d(130, 100), 0)
                        .liftUp()
                        .waitSeconds(1.4)
                        .pixelDeGripp()
                        .waitSeconds(1);
                break;
            }

            case RED_FORWARD: {
                builder.strafeToLinearHeading(new Vector2d(130, -90), 0)//left
                        //тут ставим пиксель на задник
                        .liftUp()
                        .waitSeconds(1.4)
                        .pixelDeGripp()
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(70, -30), 0)
                        .strafeToLinearHeading(new Vector2d(-80, -30), 0)
                        //врубили щётки
                        //начали опускать
                        .brushDown()
                        .brushOn()
                        .strafeToLinearHeading(new Vector2d(-145, -60), 0)
                        //.strafeToLinearHeading(new Vector2d(-145, -40), 0)
                        //хаваем стопки
                        .waitSeconds(5)
                        .strafeToLinearHeading(new Vector2d(60, -30), 0)
                        .waitSeconds(0.2)
                        .strafeToLinearHeading(new Vector2d(130, -100), 0)
                        .liftUp()
                        .waitSeconds(1.4)
                        .pixelDeGripp()
                        .waitSeconds(1);
                break;

            }
        }

        return builder;
    }
}
/*

package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    private  static double RedForwardCameraY = 0;
    private  static double LeftPose = -70;
    private  static double RightPose = -110;
    private  static double ForwardPose = -90;


    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        builder.brushOn();
        switch (camera){
            case LEFT:
                RedForwardCameraY = LeftPose;
                break;
            case RIGHT:
                RedForwardCameraY =RightPose;
                break;
            case FORWARD:
                RedForwardCameraY =ForwardPose;
                break;
        }

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
                builder.strafeToLinearHeading(new Vector2d(130, RedForwardCameraY), 0)//left
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


 */