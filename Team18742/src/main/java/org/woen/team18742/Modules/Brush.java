package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;

@Config
public class Brush {
    private DcMotorEx brushMotor;
    private boolean flagdefense = true;

    public static double getvolteges = 1;
    public static double timesxz = 1500;
    public static double times1 = 3000;

    ElapsedTime elapsedTime = new ElapsedTime();

    private BaseCollector _collector;

    public Brush(BaseCollector collector){
        _collector = collector;

        brushMotor = _collector.CommandCode.hardwareMap.get(DcMotorEx.class, "brushMotor");

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void intakePowerWithDefense(boolean brush1, double speed) {//функция для щёток с зашитой от зажёвывания
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

    public void intakePowerWithDefense(boolean brush1) {//функция для щёток с зашитой от зажёвывания
        double speed = 1.00;
        intakePowerWithDefense(brush1, speed);

    }
}
