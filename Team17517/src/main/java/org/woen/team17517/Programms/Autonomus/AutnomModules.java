package org.woen.team17517.Programms.Autonomus;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.Intake.State;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

import java.util.List;

public class AutnomModules {
    UltRobot robot;
    PipeLine pipeLine;

    public AutnomModules(UltRobot robot) {
        this.robot = robot;
    }

    public void move(double x, double y, double h, double time) {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveAngle(h),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(x, y),
                () -> robot.timer.getTimeForTimer(time),
                () -> robot.driveTrainVelocityControl.moveWithAngleControl(0, 0),
                () -> robot.timer.getTimeForTimer(0.1),

        });
    }

    List<Trajectory> trajectories = robot.mover.builder()
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
    public void scoring() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.scoring(),
                () -> robot.timer.getTimeForTimer(0.5)
        });
    }

    public void backdropLow() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.setState(State.WAITINGBACKDROPDOWN),
        });
    }

    public void eatWhite(){
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.setState(State.EATING)
        });
    }
    public void reverseEatWhite(){
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.intake.setState(State.REVERSINGAFTEREATING)
        });
    }

    public void bacBoardPixels() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.grabber.close(),
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
                () -> robot.grabber.down()
        });
    }

    public void timer(double time){
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.timer.getTimeForTimer(time),
        });
    }

    public void eatPixels() {
        robot.updateWhilePositionFalse(new Runnable[]{
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.in(),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.grabber.close(),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.brush.out(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.brush.off(),
                () -> robot.timer.getTimeForTimer(0.1),
        });
    }
}
