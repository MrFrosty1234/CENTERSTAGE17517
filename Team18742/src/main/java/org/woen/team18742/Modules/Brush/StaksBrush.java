package org.woen.team18742.Modules.Brush;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Tools.Devices;

public class StaksBrush implements IRobotModule {
    private boolean pixelOnLift = false;
    private DcMotorEx StacksBrushMotor;
    public double downServoPos = 0;
    public double upServoPos = 1;
    private StakBrush stateStakBrush = StakBrush.STATE_OFF;
private double UpTime = 1000;
private double ReversTime = 1000;
    private PosBrush posbrush = PosBrush.STATE_UP;
    private ElapsedTime TimeToSetOnPos = new ElapsedTime();
    private ElapsedTime TimeToRevers = new ElapsedTime();
     enum PosBrush{
        STATE_DOWN,STATE_UP;
    }
    enum StakBrush{
        STATE_ON,STATE_OFF,STATE_REV;
    }
    public void UpBrush(){
         SetPosition(PosBrush.STATE_UP);

    }
    public void DownBrush(){
        SetPosition(PosBrush.STATE_DOWN);
    }
    private void SetPosition(PosBrush TargetPos){
         if(TargetPos != posbrush){
             posbrush = TargetPos;
             TimeToSetOnPos.reset();
         }
    }
    private void StakBrushSetState(StakBrush TargetState){
         if(TargetState != stateStakBrush){
             if(TargetState == StakBrush.STATE_OFF){
                 TimeToSetOnPos.reset();
             }
             if(TargetState == StakBrush.STATE_ON){

             }
             if(TargetState == StakBrush.STATE_REV){
                 TimeToRevers.reset();
             }

         }
    }
    private void servoSetPosUp(){
         //стваим подъём сервы вверх
    }
    private void servoSetPosDown(){
        //стваим подъём сервы вниз
    }
    private void normalRun(){
         //щётки на захват
    }
    private void reversRun(){
         //ревёрс щёток
    }
    private  void  stop(){
         //остановка щёток
    }
    @Override
    public void Update(){
         // значения амперов с сервы1 = StakServo1AMPS
        // значения амперов с сервы2 = StakServo2AMPS

switch (stateStakBrush){
    /* if(большие шётки включены){
    StakBrushSetState(StakBrush.STATE_ON);
    }

*/
        case STATE_ON:
            normalRun();
            if(pixelOnLift) {
                StakBrushSetState(StakBrush.STATE_OFF);
            }
            break;
    case STATE_OFF:
        SetPosition(PosBrush.STATE_UP);
        stop();
        if(TimeToSetOnPos.milliseconds() > UpTime){
            StakBrushSetState(StakBrush.STATE_REV);
        }
        break;
    case STATE_REV:
        reversRun();
        if(TimeToRevers.milliseconds() > ReversTime) {
            StakBrushSetState(StakBrush.STATE_OFF);
        }
        break;
    }
    switch (posbrush){
        case STATE_UP:
            break;
        case STATE_DOWN:
            break;
    }
}
}
