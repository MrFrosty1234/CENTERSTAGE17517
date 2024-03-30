package org.woen.team17517.Programms.Autonomus;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;

import org.woen.team17517.RobotModules.Intake.Lift.LiftPosition;
import org.woen.team17517.RobotModules.Intake.State;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

import java.util.List;

public class AutnomModules {
    UltRobot robot;
    PipeLine pipeLine;

    public AutnomModules(UltRobot robot) {
        this.robot = robot;
        trajectories = robot.mover.builder()
                .strafeTo(new Vector2d(12, 32.5))
                .strafeTo(new Vector2d(12, 47))
                .splineToLinearHeading(new Pose2d(42, 35, Math.toRadians(180)), -1)

                .strafeTo(new Vector2d(47, 24))
                .splineToConstantHeading(new Vector2d(0, 12), -3)
                .splineToConstantHeading(new Vector2d(-54, 12), 0)

                .splineToConstantHeading(new Vector2d(0, 12), 0)
                .splineToConstantHeading(new Vector2d(47, 14), 0)
                .strafeToConstantHeading(new Vector2d(42, 35))
                .build();
    }

    public void move(double x, double y, double h, double time) {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveAngle(h),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(x, y),
                () -> robot.timer.waitSeconds(time),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(0, 0),
                () -> robot.timer.waitSeconds(0.1)
        });
    }

    List<Trajectory> trajectories;

    public void scoring() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.scoring(),
                () -> robot.timer.waitSeconds(0.5)});
    }
    public void wait(int seconds){
        robot.updateWhilePositionFalse( new Runnable[]{
            ()->robot.timer.waitSeconds(seconds)
        });
    }

    public void backdropLow() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.on(),
                () -> robot.lift.auto(),
                () -> robot.intake.waitUp(LiftPosition.BACKDROPDOWN)});
    }


    public void timer(double time) {
        robot.updateWhilePositionFalse(new Runnable[]{() -> robot.timer.waitSeconds(time)});
    }

}
