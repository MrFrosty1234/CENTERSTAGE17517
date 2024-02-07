package org.woen.team18742.Modules.Brush;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Intake;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Module

public class Brush implements IRobotModule {
    private DcMotorEx brushMotor;
    private BrushState statebrush = BrushState.STATE_OFF;
    public byte trueStateBrush = 3;

    private ElapsedTime ProtTime = new ElapsedTime();
    private ElapsedTime RevTime = new ElapsedTime();
    private Lift _lift;
    private Intake _intake;

    @Override
    public void Init(BaseCollector collector) {
        brushMotor = Devices.BrushMotor;

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
    }

    private void stop() {//функция для конечного автомата
        brushMotor.setPower(0);
        trueStateBrush = 3;
    }

    private void NormalRun() {//функция для конечного автомата
        brushMotor.setPower(Configs.Brush.brushPower);
        trueStateBrush = 1;
    }

    private void reversRun() {//функция для конечного автомата
        brushMotor.setPower(Configs.Brush.brushPowerReverse);
        trueStateBrush = 2;
    }

    enum BrushState {
        STATE_ON, STATE_PROT, STATE_OFF, STATE_REV_OFF;
    }

    public boolean isBrusnOn() {
        return BrushState.STATE_ON == statebrush || BrushState.STATE_PROT == statebrush;
    }


    @Override
    public void Update() {
        double motorCurrent = brushMotor.getCurrent(CurrentUnit.AMPS);

        if (!_lift.isDown() || _intake.isPixelGripped())
           changeState(BrushState.STATE_OFF);

        switch (statebrush) {
            case STATE_ON: //тут нормальные щётки
                NormalRun();
                if (motorCurrent <= Configs.Brush.protectionCurrentAmps) {
                    ProtTime.reset();
                }
                if (ProtTime.milliseconds() > Configs.Brush.protectionTimeThresholdMs) {
                    changeState(BrushState.STATE_PROT);
                }
                break;
            case STATE_PROT://рверс
                reversRun();
                if (RevTime.milliseconds() > Configs.Brush.reverseTimeThresholdMs) {
                    changeState(BrushState.STATE_ON);
                }
                break;
            case STATE_OFF://выключение щёток

                if (RevTime.milliseconds() < Configs.Brush.reverseTimeThresholdMs) {
                    reversRun();
                } else {
                    stop();
                }
                break;

        }
    }

    public void BrushEnable() {
        changeState(BrushState.STATE_ON);
        brushMotor.setPower(0.3);
    }

    public void BrushDisable() {
        changeState(BrushState.STATE_OFF);

    }

    public void RevTimeRes() {
        RevTime.reset();
    }

    private void changeState(BrushState TargetState) {
        if (TargetState != statebrush) {
            if (TargetState == BrushState.STATE_OFF) {
                RevTimeRes();
            }
            if (TargetState == BrushState.STATE_ON) {
                ProtTime.reset();
            }
            if (TargetState == BrushState.STATE_PROT) {
                RevTimeRes();
            }
            statebrush = TargetState;
        }
    }
}