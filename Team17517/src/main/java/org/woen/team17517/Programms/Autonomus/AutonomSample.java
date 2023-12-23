package org.woen.team17517.Programms.Autonomus;

public class AutonomSample extends AutonomBaseClass {
    @Override
    public Runnable[] getBlueRight() {
        return new Runnable[]{
                () ->
                        robot.driveTrain.moveField(60, -60, 0)
                /*    if (positionEllment == 1)
                        robot.driveTrain.moveField(60, -60, 0);
                    if (positionEllment == 2)
                        robot.driveTrain.moveField(60, 0, 0);
                    if (positionEllment == 3)
                        robot.driveTrain.moveField(60, 60, 0);
                },
                () -> {
                    robot.driveTrain.moveField(60, 60, 90);
                },
                () -> {
                    robot.driveTrain.moveField(60, 200, 90);

                },
                () -> {
                    robot.driveTrain.moveField(60, 300, 90);
                },
                () -> {
                    robot.driveTrain.moveField(30, 300, 90);
                },
                () -> {
                    robot.driveTrain.moveField(30, 350, 90);
                }

                 */
        };
    }

    @Override
    public Runnable[] getBlueLeft() {
        return new Runnable[]{
                () ->
                        robot.driveTrain.moveField(60, -60, 0)
        };
    }

    @Override
    public Runnable[] getRedRight() {
        return new Runnable[]{
                () ->
                        robot.driveTrain.moveField(60, -60, 0)
                /*       if (positionEllment == 1)
                           robot.driveTrain.moveField(60, -60, 0);
                       if (positionEllment == 2)
                           robot.driveTrain.moveField(60, 0, 0);
                       if (positionEllment == 3)
                           robot.driveTrain.moveField(60, 60, 0);
                   },
                  () -> {
                       robot.driveTrain.moveField(60, 60, 90);
                   },
                   () -> {
                       robot.driveTrain.moveField(60, 200, 90);

                   },
                   () -> {
                       robot.driveTrain.moveField(60, 300, 90);
                   },
                   () -> {
                       robot.driveTrain.moveField(30, 300, 90);
                   },
                   () -> {
                       robot.driveTrain.moveField(30, 350, 90);

                   }
                 */
        };
    }

    @Override
    public Runnable[] getRedLeft() {
        return new Runnable[]{
                () -> robot.driveTrain.moveField(60, -60, 0)
        };
    }
}
