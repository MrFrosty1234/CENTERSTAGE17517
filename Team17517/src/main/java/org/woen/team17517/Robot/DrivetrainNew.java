
package org.woen.team17517.Robot;

public class DrivetrainNew implements RobotModule{

    UltRobot robot = null;

    boolean autoMode = false;

    public static double kPX = 1;
    public static double kDX = 0;
    public static double kIX = 0;
    public static double kPY = 1;
    public static double kDY = 0;
    public static double kIY = 0;
    public static double kPH = 1;
    public static double kDH = 0;
    public static double kIH = 0;
    public static double u_maxX = 0;
    public static double u_maxH = 0;
    public static double u_maxY = 0;

    private double targetX = 0;
    private double targetH = 0;
    private double targetY = 0;

    double errX = 0;
    double errY = 0;
    double errH = 0;

    public static double minx = 1;
    public static double minh = 1;
    public static double miny = 1;

    PidRegulator pidX = new PidRegulator(kPX, kIX, kDX, u_maxX);
    PidRegulator pidY = new PidRegulator(kPY, kIY, kDY, u_maxY);
    PidRegulator pidH = new PidRegulator(kPH, kIH, kDH, u_maxH);
    // y heading (с англ. поворот)

    public void setTarget(double x, double y, double h) {
        targetX = x;
        targetH = h;
        targetY = y;
        autoMode = true;

    }

  //  void enableAutoMode(boolean )


    public DrivetrainNew(UltRobot robot) {
        this.robot = robot;
    }

     public void update() {
        if (autoMode) {
            double x = robot.updateCameraAndOdometry.coords[0];
            double y = robot.updateCameraAndOdometry.coords[1];
            double h = robot.odometry.heading;
            errX = targetX - x;
            errY = targetY - y;

            errH = targetH - h;
            while (errH >= 180) {
                errH = errH - 360;
            }

            while (errH < -180) {
                errH = errH + 360;
            }

            robot.driveTrainVelocityControl.targetH = pidH.update(errH);
            robot.driveTrainVelocityControl.vector.x = pidX.update(errX);
            robot.driveTrainVelocityControl.vector.y = pidY.update(errY);
        }
    }
     public boolean isAtPosition() {
        if((errX < minx) && (errY <miny) && (errH < minh)){
            return true;
        }
        return false;
    }
}
