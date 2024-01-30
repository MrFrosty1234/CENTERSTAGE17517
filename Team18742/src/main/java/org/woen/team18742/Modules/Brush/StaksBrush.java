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
    private  Servo servoBrush1;
    private  Servo servoBrush2;
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
    private void normalRun(){
        servoBrush1.setPosition(ServoGoPose1);
        servoBrush2.setPosition(ServoGoPose2);
    }
    private void reversRun(){
        servoBrush1.setPosition(ServoGoPose2);
        servoBrush2.setPosition(ServoGoPose1);
    }
    private void stop(){
        servoBrush1.setPosition(ServoStopPose1);
        servoBrush2.setPosition(ServoStopPose2);
    }
    private void servoSetUpPose(){
        servoToUpBrush.setPosition(upServoPos);
    }
    private void servoSetDownPose(){
        servoToUpBrush.setPosition(downServoPos);
    }
    
    private boolean brushIsDown(){
        if(BrushUpState.STATE_DOWN == newState){
            return true;
        }else {
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
        if(brushIsDown()){
switch (_Brush.trueStateBrush){
    case 1:
        normalRun();
        break;
    case 2:
        reversRun();
        break;
    case 3:
        stop();
        break;
}}else{
        stop();
        }

    }
}
