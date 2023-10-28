package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.Lighting;
import org.woen.team17517.Robot.UltRobot;
import static java.lang.Math.signum;

@TeleOp
public class TeleOpM extends LinearOpMode {
    public double speed = 1.0;
    boolean oldSquare = false;
    boolean graberPosition;
    boolean oldCircle = false;
    boolean oldTriangle = false;
    boolean oldBumper = false;
    UltRobot robot;
    boolean speedcontrol = false;

    @Override
    public void runOpMode() {
        robot = new UltRobot(this);
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
                robot.lift.liftPos = !robot.lift.liftPos;
            } else if (triangle || cross) {
                robot.lift.liftMode = Lift.LiftMode.MANUALLIMIT;
                robot.lift.liftPos = !robot.lift.liftPos;
            }
            if (triangle) {
                robot.lift.power = 1.0;
                robot.lift.liftPos = !robot.lift.liftPos;
            }
            if (cross) {
                robot.lift.power = -0.1;
                robot.lift.liftPos = !robot.lift.liftPos;
            }
            if (!triangle && !cross) {
                robot.lift.power = 0.1;
                robot.lift.liftPos = !robot.lift.liftPos;
            }
            robot.odometry.update();
            robot.lift.update();


            telemetry.addData("x", robot.odometry.x);
            telemetry.addData("y", robot.odometry.y);
            telemetry.addData("heading", robot.odometry.heading);
            telemetry.addData("motor", robot.lift.liftMotor.getCurrentPosition());
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

            robot.grabber.enable(gamepad1.square);

            oldBumper = gamepad1.right_bumper;
            robot.driveTrain.setPowers(moveLikeKTM(axial),moveLikeKTM(lateral),moveLikeKTM(yaw));
            oldSquare = square;
            oldCircle = circle;
            oldTriangle = triangle;
            telemetry.update();
        }
    }
    double moveLikeKTM(double power){
        return power*power*signum(power);
    }
}
