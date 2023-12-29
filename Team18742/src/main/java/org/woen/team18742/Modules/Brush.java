package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;

@Config
public class Brush {
    private final DcMotorEx brushMotor;
    private boolean flagdefense = true;

    public static double getvolteges = 1;
    public static double timesxz = 1500;
    public static double times1 = 3000;

    private boolean _isReversed = false, _isIntake = false;

    ElapsedTime elapsedTime = new ElapsedTime();
    public Brush(BaseCollector collector){
        brushMotor = Devices.BrushMotor;

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void intakePowerWithDefense(boolean brush1, double speed) {//функция для щёток с зашитой от зажёвывания
        if (brush1) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= getvolteges && flagdefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= timesxz && flagdefense) {
                flagdefense = false;
            }
            if (elapsedTime.milliseconds() >= times1 && !flagdefense) {
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
        _isReversed = false;
        _isIntake = true;
        brushMotor.setPower(0);

        intakePowerWithDefense(true, 1);
    }

    public void Revers(){
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
}
