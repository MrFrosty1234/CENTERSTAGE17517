package org.woen.team17517.Programms.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.RobotModules.Grabber.Grabber;
import org.woen.team17517.RobotModules.Lift.LiftPosition;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;
import org.woen.team17517.Service.Vector2D;

@TeleOp
public class TeleOP extends LinearOpMode {
    UltRobot robot;
    DcMotor grabber;
    Grabber.PerekidPosition perekidPosition;

    boolean triangle = false;
    Button triangleButton = new Button();
    private  enum TeleopMode{
        ROBOT,CENTRE
    }
    TeleopMode teleopMode = TeleopMode.CENTRE;
    @Override
    public void runOpMode() {
        robot = new UltRobot(this);

        grabber = robot.linearOpMode.hardwareMap.dcMotor.get("intakeMotor");

        double forwardSpeed;
        double sideSpeed;
        double angleSpeed;

        waitForStart();



        boolean liftAtTarget = true;

        while (opModeIsActive()) {
            triangle = gamepad1.triangle;

            if (triangleButton.update(triangle)){
                if(teleopMode == TeleopMode.CENTRE){
                    teleopMode = TeleopMode.ROBOT;
                }else{
                    teleopMode = TeleopMode.CENTRE;
                }
            }
            switch (teleopMode) {
                case ROBOT:
                    forwardSpeed = -gamepad1.left_stick_y;
                    sideSpeed = gamepad1.left_stick_x;
                    angleSpeed = gamepad1.right_stick_x;

                    forwardSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(forwardSpeed);
                    sideSpeed = robot.driveTrainVelocityControl.linearVelocityPercent(sideSpeed);
                    angleSpeed = robot.driveTrainVelocityControl.angularVelocityPercent(angleSpeed);

                    robot.driveTrainVelocityControl.moveRobotCord(sideSpeed, forwardSpeed, angleSpeed);
                    break;
                case CENTRE:
                    double targetH;
                    double targetX;
                    double targetY;
                    Vector2D targetVector = new Vector2D();
                    Vector2D positionVector = robot.odometryNew.getPositionVector();
                    double hPosition = robot.odometryNew.getH();

                    targetY = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_y);
                    targetX = robot.driveTrainVelocityControl.linearVelocityPercent(gamepad1.left_stick_x);
                    targetH = robot.driveTrainVelocityControl.angularVelocityPercent(gamepad1.right_stick_x);

                    targetVector.setCord(targetX,targetY);
                    targetVector.vectorRat(hPosition);
                    targetVector = Vector2D.vectorSum(targetVector,positionVector);
                    targetH += hPosition;

                    robot.driveTrainVelocityControl.moveGlobalCord(targetVector, targetH);
                    break;
            }


            if (gamepad1.dpad_up) robot.lift.moveUP();
            else if (gamepad1.dpad_down) robot.lift.moveDown();


            if (gamepad1.left_bumper)
                grabber.setPower(1);
            else if (gamepad1.left_trigger > 0.1)
                grabber.setPower(-gamepad1.left_trigger);
            else
                grabber.setPower(0);


            if (gamepad1.a || (robot.lift.getTargetPosition() == LiftPosition.UP && !liftAtTarget))
                robot.grabber.closeGraber();
            if (gamepad1.b) robot.grabber.openGraber();


            if (robot.lift.isAtPosition()) {

                if (robot.lift.getTargetPosition() == LiftPosition.UP)
                    perekidPosition = Grabber.PerekidPosition.FINISH;
                else perekidPosition = Grabber.PerekidPosition.START;

            } else perekidPosition = Grabber.PerekidPosition.LIFT_SAFE;

            robot.grabber.setPerekidPosition(perekidPosition);

            robot.allUpdate();
        }
    }
}
