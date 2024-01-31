package org.woen.team18742.Modules.RoadRunner;

import static java.lang.Math.PI;

import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team18742.Modules.Camera.CameraRobotPosition;
import org.woen.team18742.Tools.Bios;

public class Trajectory {
    public static RoadRunnerRouteManager.MyTrajectoryBuilder GetTrajectory(RoadRunnerRouteManager.MyTrajectoryBuilder builder, CameraRobotPosition camera) {
        return builder
                .strafeTo(
                        new Vector2d(Bios.GetStartPosition().Position.X/* + 60.96*/, Bios.GetStartPosition().Position.Y - 182.8))
                .setReversed(true)
                .strafeTo(
                        new Vector2d(Bios.GetStartPosition().Position.X/* + 60.96*/, Bios.GetStartPosition().Position.Y));

        //return builder.turnTo(0);
    }
}
