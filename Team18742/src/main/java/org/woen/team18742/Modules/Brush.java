package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module

public class Brush implements IRobotModule {
    private DcMotorEx brushMotor;
    private BrushState statebrush = BrushState.STATE_OFF;
    private boolean _flagDefense = true;

    private boolean _isReversed = false, _isIntake = false;

    private ElapsedTime ProtTime = new ElapsedTime();
    private ElapsedTime RevTime = new ElapsedTime();
    private Lift _lift;
    private Intake _intake;
    private double MAX_CURRENT = 3;
    private double  PROTECTION_TIME = 2000;
    private double REVERS_TIME = 1000;
    @Override
    public void Init(BaseCollector collector){
        brushMotor = Devices.BrushMotor;

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        _lift = collector.GetModule(Lift.class);
        _intake = collector.GetModule(Intake.class);
    }

    private void intakePowerWithProtection(boolean brushOn, double speed) {//функция для щёток с зашитой от зажёвывания
        /*if (brushOn) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= Configs.Brush.protectionCurrentAmps && _flagDefense) {
                ProtTime.reset();
            }
            if (ProtTime.milliseconds() >= Configs.Brush.protectionTimeThreshold && _flagDefense) {
                _flagDefense = false;
            }
            if (ProtTime.milliseconds() >= Configs.Brush.reverseTimeThreshold && !_flagDefense) {
                _flagDefense = true;
            }
            if (!_flagDefense) {
                brushMotor.setPower(-speed);
            } else {
                brushMotor.setPower(speed);
            }
        } else {
            brushMotor.setPower(0);
        }*/
        if(brushOn)
            brushMotor.setPower(speed);
        else
            brushMotor.setPower(0);
    }

    public void IntakePowerWithProtection() {//функция для щёток с зашитой от зажёвывания
        if(!_lift.isDown())
            return;

        _isReversed = false;
        _isIntake = true;
        brushMotor.setPower(0);

        intakePowerWithProtection(true, 1);
    }

    public void Reverse(){
        if(!_lift.isDown())
            return;

        intakePowerWithProtection(false, 1);
        _isIntake = false;
        brushMotor.setPower(-1);
        _isReversed = true;
    }

    @Override
    public void Stop(){
        _isIntake = false;
        _isReversed = false;
        intakePowerWithProtection(false, 1);
    }
    private void stop(){//функция для конечного автомата
        brushMotor.setPower(0);
    }
    private void NormalRun(){//функция для конечного автомата
        brushMotor.setPower(1);
    }
    private void reversRun(){//функция для конечного автомата
        brushMotor.setPower(-1);
    }

    public boolean IsReversed(){
        return _isReversed;
    }

    public boolean IsIntake(){
        return _isIntake;
    }

    public boolean IsStopped(){
        return !_isIntake && !_isReversed;
    }

    enum BrushState{
        STATE_ON, STATE_PROT, STATE_OFF;
    }


    @Override
    public void Update(){




        if(!_lift.isDown())
            statebrush = BrushState.STATE_OFF;
        switch(statebrush){
            case STATE_ON: //тут нормальные щётки
                NormalRun();
                if (brushMotor.getCurrent(CurrentUnit.AMPS) <= MAX_CURRENT) {
                    ProtTime.reset();
                }
                if(ProtTime.milliseconds() > PROTECTION_TIME){
                    changeState(BrushState.STATE_PROT);
                }
                break;
            case STATE_PROT://рверс
                reversRun();
                if(RevTime.milliseconds() > REVERS_TIME){
                    changeState(BrushState.STATE_ON);
                }
                break;
            case STATE_OFF://выключение щёток

                if(RevTime.milliseconds() < REVERS_TIME){
                   reversRun();
                }else{
                    stop();
                }
                break;
        }
    }
public void BrushEnable(){
        changeState(BrushState.STATE_ON);
}
    public void BrashDisable(){
        changeState(BrushState.STATE_OFF);
    }
    public void BrushReverse(){
        changeState(BrushState.STATE_PROT);
    }



private void changeState(BrushState TargetState){
    if(TargetState == BrushState.STATE_OFF){
        RevTime.reset();
    }
    if(TargetState == BrushState.STATE_ON){
        ProtTime.reset();
    }
    if(TargetState == BrushState.STATE_PROT){
        RevTime.reset();
    }
}}