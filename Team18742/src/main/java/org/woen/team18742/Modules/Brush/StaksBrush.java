package org.woen.team18742.Modules.Brush;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Modules.Brush.Brush;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

public class StaksBrush implements IRobotModule {
    private Servo servoToUpBrush;
    private Servo servoBrush1;
    private Servo servoBrush2;
    private Brush _Brush;
    private Lift _lift;
    private BrushUpState newState = BrushUpState.STATE_UP;
    private Intake _intake;
    public double downServoPos = 0;
    public double upServoPos = 1;
    public double ServoGoPose1 = 0;
    public double ServoGoPose2 = 1;
    public double ServoStopPose1 = 0.5;
    public double ServoStopPose2 = 0.5;

    enum BrushUpState {
        STATE_UP, STATE_DOWN;
    }

    private void normalRun() {
        servoBrush1.setPosition(Configs.StackBrush.LEFT_SERVO_FWD);
        servoBrush2.setPosition(Configs.StackBrush.RIGHT_SERVO_FWD);
    }

    private void reversRun() {
        servoBrush1.setPosition(Configs.StackBrush.LEFT_SERVO_REV);
        servoBrush2.setPosition(Configs.StackBrush.RIGHT_SERVO_REV);
    }

    private void stop() {
        servoBrush1.setPosition(Configs.StackBrush.LEFT_SERVO_STOP);
        servoBrush2.setPosition(Configs.StackBrush.RIGHT_SERVO_STOP);
    }

    private void servoSetUpPose() {
        servoToUpBrush.setPosition(Configs.StackBrush.SERVO_LIFT_UP);
    }

    private void servoSetDownPose() {
        servoToUpBrush.setPosition(Configs.StackBrush.SERVO_LIFT_DOWN);
    }

    private boolean brushIsDown() {
        if (BrushUpState.STATE_DOWN == newState) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void Init(BaseCollector collector) {
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _Brush = collector.GetModule(Brush.class);

    }

    @Override
    public void Update() {
        if (brushIsDown()) {
            switch (_Brush.trueStateBrush) {
                case 1:
                    normalRun();
                    break;
                case 2:
                    reversRun();
                    break;
                case 3:
                    stop();
                    break;
            }
        } else {
            stop();
        }

    }
}
