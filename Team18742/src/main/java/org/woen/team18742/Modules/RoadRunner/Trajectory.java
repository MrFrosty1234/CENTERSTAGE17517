package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        return builder
                .splineTo(new Vector2d(Bios.GetStartPosition().Position.X, Bios.GetStartPosition().Position.Y - 60.96), 0)
                .setReversed(true)
                .splineTo(new Vector2d(Bios.GetStartPosition().Position.X, Bios.GetStartPosition().Position.Y), -PI / 2);
                //.turnTo(0);
    }
}
