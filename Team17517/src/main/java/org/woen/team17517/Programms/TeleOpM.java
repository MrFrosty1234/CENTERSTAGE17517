package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.Lighting;
import org.woen.team17517.Robot.Robot;

@TeleOp
public class TeleOpM extends LinearOpMode {
    public double speed = 1.0;
    boolean oldSquare = false;
    boolean graberPosition;
    boolean oldCircle = false;
    boolean oldTriangle = false;
    boolean oldBumper = false;
    Robot robot;
    boolean speedcontrol = false;

    @Override
    public void runOpMode() {
        robot = new Robot(this);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;
        robot.lighting.lightMode = Lighting.LightningMode.SMOOTH;
        robot.lift.liftPosition = Lift.LiftPosition.ZERO;
        while (opModeIsActive()) {
            boolean circle = gamepad1.circle;
            boolean square = gamepad1.square;
            boolean triangle = gamepad1.triangle;
            boolean cross = gamepad1.cross;

            if (square && !oldSquare) {
                graberPosition = !graberPosition;
            }
            if (gamepad1.ps) {
                robot.lift.reset();
            }

            robot.grabber.setPosition(graberPosition);
            if (gamepad1.dpad_down) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.ZERO;
            }

            if (gamepad1.dpad_up) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.UP;
            }

            if (gamepad1.dpad_left) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.LOW;
            }
            if (gamepad1.dpad_right) {
                robot.lift.liftMode = Lift.LiftMode.AUTO;
                robot.lift.liftPosition = Lift.LiftPosition.MIDDLE;
            }

            if (gamepad1.left_trigger > 0.1) {
                robot.lift.liftMode = Lift.LiftMode.MANUAL;
            } else if (triangle || cross) {
                robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;
            }
            if (triangle) {
                robot.lift.power = 1.0;
            }
            if (cross) {
                robot.lift.power = -0.1;
            }
            if (!triangle && !cross) {
                robot.lift.power = 0.1;
            }
            robot.odometry.update();
            robot.lift.update();


            telemetry.addData("x", robot.odometry.x);
            telemetry.addData("y", robot.odometry.y);
            telemetry.addData("heading", robot.odometry.heading);
            telemetry.addData("motor1", robot.lift.liftMotor1.getCurrentPosition());
            telemetry.addData("motor2", robot.lift.liftMotor2.getCurrentPosition());
            telemetry.addData("top", robot.lift.buttonUp.getState());
            telemetry.addData("down", robot.lift.buttonDown.getState());
            robot.lighting.update();
            robot.driveTrain.displayEncoders();
            double axial = -gamepad1.left_stick_y * speed;
            double lateral = -gamepad1.left_stick_x * speed;
            double yaw = -gamepad1.right_stick_x * speed;

            if (gamepad1.right_bumper) {
                axial /= 3;
                lateral /= 3;
                yaw /= 3;
            }
            oldBumper = gamepad1.right_bumper;
            robot.driveTrain.setPowers(axial, lateral, yaw);
            oldSquare = square;
            oldCircle = circle;
            oldTriangle = triangle;
            telemetry.update();
        }
    }
}
