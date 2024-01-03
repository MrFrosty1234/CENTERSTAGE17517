package org.woen.team18742.Modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Manager.RobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

@Module
public class Brush extends RobotModule {
    private DcMotorEx brushMotor;
    private boolean _flagDefense = true;

    private boolean _isReversed = false, _isIntake = false;

    private ElapsedTime elapsedTime = new ElapsedTime();

    private Lift _lift;

    @Override
    public void Init(BaseCollector collector){
        brushMotor = Devices.BrushMotor;

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        _lift = collector.GetModule(Lift.class);
    }

    private void intakePowerWithProtection(boolean brushOn, double speed) {//функция для щёток с зашитой от зажёвывания
        if (brushOn) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= Configs.Brush.protectionCurrentAmps && _flagDefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= Configs.Brush.protectionTimeThreshold && _flagDefense) {
                _flagDefense = false;
            }
            if (elapsedTime.milliseconds() >= Configs.Brush.reverseTimeThreshold && !_flagDefense) {
                _flagDefense = true;
            }
            if (!_flagDefense) {
                brushMotor.setPower(-speed);
            } else {
                brushMotor.setPower(speed);
            }
        } else {
            brushMotor.setPower(0);
        }
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

    public boolean IsReversed(){
        return _isReversed;
    }

    public boolean IsIntake(){
        return _isIntake;
    }

    public boolean IsStopped(){
        return !_isIntake && !_isReversed;
    }

    @Override
    public void Update(){
        if(!_lift.isDown())
            Stop();
    }
}
