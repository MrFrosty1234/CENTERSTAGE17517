package org.woen.team17517.RobotModules.Intake;


import static org.woen.team17517.RobotModules.Intake.State.*;

import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.RobotModules.EndGame.Hang;
import org.woen.team17517.RobotModules.EndGame.HangPower;
import org.woen.team17517.RobotModules.Intake.Grabber.Brush;
import org.woen.team17517.RobotModules.Intake.Grabber.GrabberNew;
import org.woen.team17517.RobotModules.Intake.Grabber.OpticalSensor;
import org.woen.team17517.RobotModules.Intake.Lift.Lift;
import org.woen.team17517.RobotModules.Intake.Lift.LiftPosition;
import org.woen.team17517.RobotModules.Lighting.Lighting;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class Intake implements RobotModule {
    UltRobot robot;

    public Intake(UltRobot robot) {
        this.robot = robot;
        grabber = robot.grabber;
        lift = robot.lift;
        brush = robot.brush;
        opticalSensor = robot.opticalSensor;
        light = robot.lighting;
        hang = robot.hang;
    }

    GrabberNew grabber;
    Lift lift;
    Brush brush;
    OpticalSensor opticalSensor;
    Lighting light;
    Hang hang;
    private boolean isOn = false;

    public void on() {
        isOn = true;
    }

    public void off() {
        isOn = false;
    }

    public void setState(State state) {
        this.state = state;
    }

    LiftPosition upPos = LiftPosition.UP;

    public void waitUp(LiftPosition pos) {
        state = WAIT_UP;
        upPos = pos;
    }

    public State getState() {
        return state;
    }

    State state = State.WAIT_DOWN;

    public void scoring() {
        state = State.SCORING_TWO;
        opticalSensor.startFreeTime = System.currentTimeMillis();
    }

    public void upHang(HangPower power) {
        hangPower = power;
        state = END_GAME;
    }

    public void hangReset() {
        startHangTime = System.currentTimeMillis();
    }

    private double startReversTime = System.currentTimeMillis();
    private double startSaveTime = System.currentTimeMillis();
    private double startOffDefenseStart = System.currentTimeMillis();
    private double startHangTime = System.currentTimeMillis();
    private HangPower hangPower = HangPower.ZERO;

    public void update() {
        if (isOn) {
            switch (state) {
                case SAVE_BRUSH:
                    light.off();
                    if (System.currentTimeMillis() - startSaveTime < 2000) brush.out();
                    else {
                        startOffDefenseStart = System.currentTimeMillis();
                        setState(EAT);
                    }
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case EAT:
                    light.on();
                    if ((robot.hardware.intakeAndLiftMotors.brushMotor.getCurrent(CurrentUnit.AMPS) > 4) &&
                            System.currentTimeMillis() - startOffDefenseStart > 1000) {
                        startSaveTime = System.currentTimeMillis();
                        setState(SAVE_BRUSH);
                    }
                    if (opticalSensor.isPixels(400)) {
                        robot.linearOpMode.gamepad1.rumble(400);
                        startReversTime = System.currentTimeMillis();
                        setState(REVERS_AFTER_EAT);
                    } else if (lift.buttonDown.getState()) {
                        brush.in();
                        grabber.backWallClose();
                        grabber.open();
                        grabber.down();
                    }
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case REVERS_AFTER_EAT:
                    light.off();
                    grabber.close();
                    if (System.currentTimeMillis() - startReversTime < 1700) brush.out();
                    else setState(WAIT_DOWN);
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case WAIT_DOWN:
                    light.off();
                    lift.moveDown();
                    brush.off();
                    grabber.down();
                    grabber.close();
                    grabber.backWallClose();
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case WAIT_UP:
                    light.off();
                    lift.move(upPos);
                    brush.off();
                    if (lift.getPosition() > 450) grabber.finish();
                    grabber.close();
                    grabber.backWallClose();
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case SCORING_TWO:
                    if (lift.getPosition() > 200) {
                        light.off();
                        grabber.finish();
                        grabber.open();
                        grabber.backWallOpen();
                        if (opticalSensor.isFree(1000)) {
                            setState(WAIT_DOWN);
                        }
                    } else {
                        setState(WAIT_DOWN);
                    }
                    robot.hardware.driveTrainMotors.allStart();
                    break;
                case END_GAME:
                    light.off();
                    brush.off();
                    if (hangPower == HangPower.UP) {
                        lift.move(LiftPosition.LOW);
                        if (System.currentTimeMillis() - startHangTime > 500) grabber.finish();
                    } else if (hangPower == HangPower.DOWN) {
                        grabber.finish();
                        lift.moveDown();
                        robot.linearOpMode.hardwareMap.getAll(PwmControl.class).forEach(PwmControl::setPwmDisable);
                        robot.hardware.driveTrainMotors.allStop();
                    }
                    if (System.currentTimeMillis() - startHangTime > 1000) {
                        if (hangPower == HangPower.UP && robot.hardware.hanging.hangingMotor.getCurrent(CurrentUnit.AMPS) > 2) {
                            hangPower = HangPower.ZERO;
                            hang.setHangPower(hangPower);
                        } else
                            hang.setHangPower(hangPower);
                    }
                    break;
                case REVERSEBRUSH:
                    brush.out();
                    break;
            }
        }
    }
}
