package org.woen.team17517.Robot;
public class Telemetry {
    UltRobot robot;
    public static boolean lift = false;
    public static boolean driveTrain = false;
    public static boolean grabber = false;

    public Telemetry(UltRobot robot){
        this.robot = robot;
    }
    void update(){
        if(lift) {
            robot.linearOpMode.telemetry.addData("liftEncs", robot.lift.liftMotor.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("target",robot.lift.liftPosition.value);
            robot.linearOpMode.telemetry.addData("lift pos",robot.lift.liftPosition);
            robot.linearOpMode.telemetry.addData("lift mode", robot.lift.liftMode);
        }
        if(driveTrain){
            robot.linearOpMode.telemetry.addData("error X", robot.driveTrain.xError);
            robot.linearOpMode.telemetry.addData("error Y", robot.driveTrain.yError);
            robot.linearOpMode.telemetry.addData("error Heading", robot.driveTrain.headingError);
            robot.linearOpMode.telemetry.addData("encoder left back",robot.driveTrain.left_back_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder right back",robot.driveTrain.right_back_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder left front", robot.driveTrain.left_front_drive.getCurrentPosition());
            robot.linearOpMode.telemetry.addData("encoder right frony", robot.driveTrain.right_front_drive.getCurrentPosition());
        }
        if(grabber) {
            robot.linearOpMode.telemetry.addData("pixels count",robot.grabber.pixelsCount);
            robot.linearOpMode.telemetry.addData("pixelSensorLeft",robot.grabber.pixelSensorLeft);
            robot.linearOpMode.telemetry.addData("pixelSensorRight",robot.grabber.pixelSensorRight);
        }
         robot.linearOpMode.telemetry.update();
    }
}
