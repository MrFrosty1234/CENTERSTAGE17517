package org.woen.team17517.Programms.Autonomus;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;

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
        robot.updateWhilePositionFalse(
                () -> robot.driveTrainVelocityControl.moveAngle(h),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(x, y),
                () -> robot.timer.getTimeForTimer(time),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(0, 0),
                () -> robot.timer.getTimeForTimer(0.1));
    }

    List<Trajectory> trajectories ;
    public void scoring() {
        robot.updateWhilePositionFalse(
                () -> robot.intake.scoring(),
                () -> robot.timer.getTimeForTimer(0.5));
    }

    public void backdropLow() {
        robot.updateWhilePositionFalse(() -> robot.intake.setState(State.WAITINGBACKDROPDOWN));
    }

    public void eatWhite(){
        robot.updateWhilePositionFalse(() -> robot.intake.setState(State.EAT));
    }
    public void reverseEatWhite(){
        robot.updateWhilePositionFalse(() -> robot.intake.setState(State.REVERS_AFTER_EAT));
    }

    public void bacBoardPixels() {
        robot.updateWhilePositionFalse(() -> robot.grabber.close(),
                () -> robot.grabber.safe(),
                () -> robot.timer.getTimeForTimer(0.2),
                () -> robot.lift.moveUP(),
                () -> move(0, 20000, 0, 1),
                () -> robot.grabber.finish(),
                () -> move(0, -20000, 0, 1),
                () -> robot.grabber.open(),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.grabber.safe(),
                () -> robot.lift.moveDown(),
                () -> robot.grabber.down());
    }

    public void timer(double time){
        robot.updateWhilePositionFalse(() -> robot.timer.getTimeForTimer(time));
    }

    public void eatPixels() {
        robot.updateWhilePositionFalse(
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.in(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.close(),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.off(),
                () -> robot.timer.getTimeForTimer(0.1));
    }
}
