package org.woen.team18742.Modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
@Config
public class Intake {
    private Servo servopere;
    private DcMotorEx brushMotor; // ц
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltage = 1.65;
    boolean inableIntake;
    private boolean flagdefense = true;
    ElapsedTime elapsedTime = new ElapsedTime();

    public Intake( BaseCollector collector) {
        _collector = collector;
        pixelSensor = _collector.CommandCode.hardwareMap.get(AnalogInput.class, "pixelSensor");
        gripper = _collector.CommandCode.hardwareMap.get(Servo.class, "gripok");
        clamp = _collector.CommandCode.hardwareMap.get(Servo.class, "gripokiu");
        servopere = _collector.CommandCode.hardwareMap.get(Servo.class, "perevert");
        brushMotor = _collector.CommandCode.hardwareMap.get(DcMotorEx.class, "brushMotor");

        brushMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

   public static double servoperevorotnazad = 0.16;
    public static double servoperevorot = 0.765;

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
        /*if (grip) {
            speed = -1;
            gripper.setPosition(servoGripper);
        } else {
            speed = 1;
            gripper.setPosition(servoGripperreturn);
        }*/
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

    public void SetGrabber(){
        if(_isGrabberOn)
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

    public static double servoClamp = 0.7;
    public static double servoClampreturn = 0.2;

    private void setClamp(boolean clampIk) {
        /*if (clampIk) {
            gripper.setPosition(servoClamp);
        } else {
            gripper.setPosition(servoClampreturn);
        }*/
    }


    ElapsedTime pixelTimer = new ElapsedTime();
    double pixelTimeconst = 500;

    public boolean pixelDetected() {
        boolean sensorValue = pixelSensor.getVoltage() > pixelSensorvoltage;
        if (!sensorValue)
            pixelTimer.reset();
        return pixelTimer.milliseconds() > pixelTimeconst;
    }

    ElapsedTime clampTimer = new ElapsedTime();
    double clampTimerconst = 500;

    public void Update() {
       clampTimer.reset();
       if (pixelDetected()) {
            setGripper(true);
            setClamp(clampTimer.milliseconds() < clampTimerconst);
            intakePower(false);
        } else {
        setGripper(false);
          setClamp(true);
            if (inableIntake)
                intakePower(true);
        }
    }
}

