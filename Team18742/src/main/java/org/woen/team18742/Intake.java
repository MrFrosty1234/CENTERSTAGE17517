package org.woen.team18742;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Intake {
    private DcMotorEx brushMotor; // ц
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltagekoksik = 1.65;
    boolean inableIntake;
    private boolean flagdefense = true;
    ElapsedTime elapsedTime = new ElapsedTime();

    public Intake(BaseCollector collector) {
        _collector = collector;

        brushMotor = _collector.CommandCode.hardwareMap.get(DcMotorEx.class, "brushMotor");
    }

    private double speed;
    private double servoGripperreturn = 0.7;
    private double servoGripper = 0.2;

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

    private void intakePowerWithDefense(boolean brush1) {//функция для щёток с зашитой от зажёвывания
        int speed = 100;
        if (brush1) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= 0.75 && flagdefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= 1.5) {
                flagdefense = false;
            }
            if (elapsedTime.milliseconds() >= 3) {
                flagdefense = true;
            }
            if (!flagdefense) {
                brushMotor.setPower(-speed);
            } else {
                brushMotor.setPower(speed);
            }

        }
    }
    private void intakePowerWithDefense(boolean brush1) {//функция для щёток с зашитой от зажёвывания

        if (brush1) {
            if (brushMotor.getCurrent(CurrentUnit.AMPS) <= 0.75 && flagdefense) {
                elapsedTime.reset();
            }
            if (elapsedTime.milliseconds() >= 1.5) {
                flagdefense = false;
            }
            if (elapsedTime.milliseconds() >= 3) {
                flagdefense = true;
            }
            if (!flagdefense) {
                brushMotor.setPower(-speed);
            } else {
                brushMotor.setPower(speed);
            }

        }
    }

    private double servoClamp = 0.7;
    private double servoClampreturn = 0.2;

    private void setClamp(boolean clampIk) {
        if (clampIk) {
            gripper.setPosition(servoClamp);
        } else {
            gripper.setPosition(servoClampreturn);
        }
    }

    public boolean pixelDetected() {
        return pixelSensor.getVoltage() > pixelSensorvoltagekoksik;
    }

    public void Update() {
        if (pixelDetected()) {
            setClamp(true);
            if (inableIntake)
                intakePower(true);
        } else {
            setClamp(false);
            {
                intakePower(false);
            }
        }
    }
}

