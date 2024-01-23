package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutonomSample extends AutonomBaseClass {
    @Override
    public Runnable[] getRedRight() {
        return new Runnable[]{
                () -> {
                    if (positionOfPixel == 3) {
                        robot.driveTrainVelocityControl.moveRobotCord(-600, -950, 0);
                    } else if (positionOfPixel == 2) {
                        robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0);
                    } else if (positionOfPixel == 1) {
                        robot.driveTrainVelocityControl.moveRobotCord(0, -730, 0);
                    }
                },
                () -> robot.timer.getTimeForTimer(1.2),
                () -> {
                    if (positionOfPixel == 1)
                        robot.driveTrainVelocityControl.moveRobotCord(600, -700, 0);
                },
                () -> {
                    if (positionOfPixel == 1)
                        robot.timer.getTimeForTimer(1);
                },
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(0.2),

                ///////////////////////////////////////////////
                () -> {
                        if (positionOfPixel == 1)
                        robot.driveTrainVelocityControl.moveRobotCord(-1000, 0, 0);
                },
                () -> {
                    if (positionOfPixel == 1)
                        robot.timer.getTimeForTimer(1);
                },
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(-1200, 0, 0),
                () -> robot.timer.getTimeForTimer(1.8),
                () -> robot.driveTrainVelocityControl.moveRobotCord(1200, 0, 0),
                () -> robot.timer.getTimeForTimer(0.5),
                ///////////////////////////////////////////////

                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 480),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0,1000,0),
            () -> robot.timer.getTimeForTimer(4)

        };
    }


    @Override
    public Runnable[] getRedLeft() {
        return new Runnable[]{
                /* () -> robot.driveTrainVelocityControl.moveRobotCord(1000, 0, 0),
                 () -> robot.timer.getTimeForTimer(1.25),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                 () -> robot.timer.getTimeForTimer(0.5),
                 () -> robot.lift.moveUP(),
                 () -> robot.timer.getTimeForTimer(1),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                 () -> robot.timer.getTimeForTimer(1),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                 () -> robot.timer.getTimeForTimer(0.5),
                 () -> robot.grabber.perekidFinish(),
                 () -> robot.timer.getTimeForTimer(1),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                 () -> robot.timer.getTimeForTimer(1),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                 () -> robot.timer.getTimeForTimer(0.5),
                 () -> robot.grabber.openGraber(),
                 () -> robot.timer.getTimeForTimer(1.5),
                 () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                 () -> robot.timer.getTimeForTimer(0.0)
         };

                 */

        };

}


    @Override
    public Runnable[] getBlueRight() {
        return new Runnable[]{
                () -> {
                    if (positionOfPixel == 1) {
                        robot.driveTrainVelocityControl.moveRobotCord(500, -950, 0);
                    } else if (positionOfPixel == 2) {
                        robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0);
                    } else if (positionOfPixel == 3) {
                        robot.driveTrainVelocityControl.moveRobotCord(0, -730, 0);
                    }
                },
                () -> robot.timer.getTimeForTimer(1.2),
                () -> {
                    if (positionOfPixel == 3)
                        robot.driveTrainVelocityControl.moveRobotCord(-600, -700, 0);
                },
                () -> {
                    if (positionOfPixel == 3)
                        robot.timer.getTimeForTimer(1);
                },
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(0.2),

                ///////////////////////////////////////////////
                () -> {
                        robot.driveTrainVelocityControl.moveRobotCord(1000, 0, 0);
                },
                () -> {
                        robot.timer.getTimeForTimer(1);
                },
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(0.3    ),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.4),
                ///////////////////////////////////////////////
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, -480),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.lift.moveUP(),
                () -> robot.grabber.perekidFinish(),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(2),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.1),
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1.5)

        };
    }

    @Override
    public Runnable[] getBlueLeft() {
        return new Runnable[]{
                () -> robot.driveTrainVelocityControl.moveRobotCord(-1000, 0, 0),
                () -> robot.timer.getTimeForTimer(1.25),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.lift.moveUP(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.grabber.perekidFinish(),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, -1000, 0),
                () -> robot.timer.getTimeForTimer(1),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 0, 0),
                () -> robot.timer.getTimeForTimer(0.5),
                () -> robot.grabber.openGraber(),
                () -> robot.timer.getTimeForTimer(1.5),
                () -> robot.driveTrainVelocityControl.moveRobotCord(0, 1000, 0),
                () -> robot.timer.getTimeForTimer(0.0)


        };
    }
}
