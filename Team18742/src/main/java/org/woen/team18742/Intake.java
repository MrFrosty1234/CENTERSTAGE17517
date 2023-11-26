package org.woen.team18742;


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    private DcMotor brushMotor; // ц
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltagekoksik = 1.65;
    boolean inableIntake;

    public Intake(BaseCollector collector) {
        _collector = collector;

        brushMotor = _collector.CommandCode.hardwareMap.get(DcMotor.class, "brushMotor");
    }
    double speed;
    double servoGripperreturn = 0.7;
    double servoGripper = 0.2;

    void setGripper(boolean grip) {
        if (grip) {
            speed = -1;
            gripper.setPosition(servoGripper);
        } else {
            speed = 1;
            gripper.setPosition(servoGripperreturn);
        }
    }

    void intakePower(boolean brush) {
        if (brush) {
            speed = 1;
            brushMotor.setPower(speed);
            inableIntake = brush;
        } else {
            speed = 0;
            brushMotor.setPower(speed);
        }
    }

    double servoClamp = 0.7;
    double servoClampreturn = 0.2;

    void setClamp(boolean clampIk) {
        if (clampIk) {
            gripper.setPosition(servoClamp);
        } else {
            gripper.setPosition(servoClampreturn);
        }
    }

    boolean pixelDetected() {
        return pixelSensor.getVoltage() > pixelSensorvoltagekoksik;
    }

    void update() {
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

