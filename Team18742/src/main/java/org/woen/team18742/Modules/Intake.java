package org.woen.team18742.Modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Intake {
    private Servo servopere;
    private DcMotorEx brushMotor; // ц
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltage = 0.4;//1.65
    boolean inableIntake;
    private boolean flagdefense = true;
    ElapsedTime elapsedTime = new ElapsedTime();

    public Intake(BaseCollector collector) {
        _collector = collector;
        pixelSensor1 = _collector.CommandCode.hardwareMap.get(AnalogInput.class, "pixelSensor1");
        pixelSensor2 = _collector.CommandCode.hardwareMap.get(AnalogInput.class, "pixelSensor2");
        gripper = _collector.CommandCode.hardwareMap.get(Servo.class, "gripok");
        clamp = _collector.CommandCode.hardwareMap.get(Servo.class, "gripokiu");
        servopere = _collector.CommandCode.hardwareMap.get(Servo.class, "perevert");
        brushMotor = _collector.CommandCode.hardwareMap.get(DcMotorEx.class, "brushMotor");

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public static double servoperevorotnazad = 0.18;//16
    public static final double servoperevorot = 0.765;

    public void setperevorotik(boolean perevert) {
        if (perevert) {
            servopere.setPosition(servoperevorotnazad);
        } else {
            servopere.setPosition(servoperevorot);
        }
    }

    double speed;
    public static double servoGripperreturn = 0.43;
    public static double servoGripper = 0.221;

    public void setGripper(boolean grip) {
        if (grip) {
            speed = -1;
            gripper.setPosition(servoGripper);
        } else {
            speed = 1;
            gripper.setPosition(servoGripperreturn);
        }
    }

    private void intakePower(boolean brush) {
        if (brush) {
            speed = 1;
            brushMotor.setPower(speed);
            inableIntake = brush;
        } else {
            speed = 0;
            brushMotor.setPower(speed);
        }
    }

    private boolean _isGrabberOn = false;

    public void SetGrabber() {
        if (_isGrabberOn)
            _isGrabberOn = false;
        else
            _isGrabberOn = true;

        intakePowerWithDefense(_isGrabberOn);
    }

    public void intakePowerWithDefense(boolean brush1) {//функция для щёток с зашитой от зажёвывания
        double speed = 1.00;
        intakePowerWithDefense(brush1, speed);
    }

    public static double getvolteges = 1;
    public static double timesxz = 1500;
    public static double times1 = 3000;

    public void intakePowerWithDefense(boolean brush1, double speed) {//функция для щёток с зашитой от зажёвывания
        if (brush1) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= getvolteges && flagdefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= timesxz) {
                flagdefense = false;
            }
            if (elapsedTime.milliseconds() >= times1) {
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

    public static double servoClamp = 0.44;
    public static double servoClampreturn = 0.1;

    public void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(servoClamp);
        } else {
            clamp.setPosition(servoClampreturn);
        }
    }


    ElapsedTime pixelTimer = new ElapsedTime();
    double pixelTimeconst = 500;

    public boolean pixelDetected() {
       // return pixelSensor1.getVoltage() <= pixelSensorvoltage && pixelSensor2.getVoltage() <= pixelSensorvoltage;
      if(pixelSensor1.getVoltage() >= pixelSensorvoltage || pixelSensor2.getVoltage() >= pixelSensorvoltage)
           pixelTimer.reset();
        return pixelTimer.milliseconds() > pixelTimeconst;
    }

    ElapsedTime clampTimer = new ElapsedTime();
    double clampTimerconst = 1500;
    public void Update() {
         clampTimer.reset();
        if (pixelDetected()) {
            clampTimer.reset();
            setGripper(true);
            setClamp(clampTimer.milliseconds() < clampTimerconst);
            intakePower(false);
        } else {
            setGripper(false);
            setClamp(true);
            if (inableIntake)
                intakePower(true);


        }



        ToolTelemetry.AddLine(pixelSensor1.getVoltage() + " " + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine(pixelDetected()+"");
    }
}


