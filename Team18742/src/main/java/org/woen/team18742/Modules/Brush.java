package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;

public class Brush {
    private final DcMotorEx brushMotor;
    private boolean flagdefense = true;

    private boolean _isReversed = false, _isIntake = false;

    private ElapsedTime elapsedTime = new ElapsedTime();

    private BaseCollector _collector;

    public Brush(BaseCollector collector){
        brushMotor = Devices.BrushMotor;

        _collector = collector;

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void intakePowerWithDefense(boolean brush1, double speed) {//функция для щёток с зашитой от зажёвывания
        if (brush1) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= Configs.Brush.getvolteges && flagdefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= Configs.Brush.timesxz && flagdefense) {
                flagdefense = false;
            }
            if (elapsedTime.milliseconds() >= Configs.Brush.times1 && !flagdefense) {
                flagdefense = true;
            }
            if (!flagdefense) {
                brushMotor.setPower(-speed);
            } else {
                brushMotor.setPower(speed);
            }
        } else {
            brushMotor.setPower(0);
        }
    }

    public void IntakePowerWithDefense() {//функция для щёток с зашитой от зажёвывания

        if(!_collector.Lift.isDown())
            return;

        _isReversed = false;
        _isIntake = true;
        brushMotor.setPower(0);

        intakePowerWithDefense(true, 1);
    }

    public void Revers(){
        if(!_collector.Lift.isDown())
            return;

        intakePowerWithDefense(false, 1);
        _isIntake = false;
        brushMotor.setPower(-1);
        _isReversed = true;
    }

    public void Stop(){
        _isIntake = false;
        _isReversed = false;
        intakePowerWithDefense(false, 1);
    }

    public boolean IsRevers(){
        return _isReversed;
    }

    public boolean IsIntake(){
        return _isIntake;
    }

    public boolean IsStop(){
        return !_isIntake && !_isReversed;
    }

    public void Update(){
        if(!_collector.Lift.isDown())
            Stop();
    }
}
