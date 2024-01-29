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
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

public class StaksBrush implements IRobotModule {
    private Servo servoToUpBrush;
    private  Servo servoBrush;
private Brush _Brush;
    private Lift _lift;
    private Intake _intake;
    public double downServoPos = 0;
    public double upServoPos = 1;
    enum BrushState {
        STATE_ON, STATE_PROT, STATE_OFF;
    }
    @Override
    public void Init(BaseCollector collector) {
        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
        _Brush = collector.GetModule(Brush.class);

    }
    @Override
    public void Update() {
switch (_Brush.trueStateBrush){
    case 1:
    break;
    case 2:
        break;
}
    }
}
