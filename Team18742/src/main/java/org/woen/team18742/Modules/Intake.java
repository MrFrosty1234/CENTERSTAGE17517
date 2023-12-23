package org.woen.team18742.Modules;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Intake {
    private Servo servopere;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltage = 0.15, PixelCenterOpen = 0;//0.4
    boolean inableIntake;
    private final DcMotor _lighting;

    public Intake(BaseCollector collector) {
        _collector = collector;
        pixelSensor1 = Devices.PixelSensor1;
        pixelSensor2 = Devices.PixelSensor2;
        gripper = Devices.Gripper;
        clamp = Devices.Clamp;
        servopere = Devices.Servopere;
        _lighting = Devices.LightingMotor;
    }

    public static double servoperevorotnazad = 0.94;
    public static double servoperevorot = 0.32;//0.5

    public void setperevorotik() {
        if (_collector.Lift.isMoveAverage() || _collector.Lift.isAverage()) {
            servopere.setPosition(servoperevorot);
        } else {
                servopere.setPosition(servoperevorotnazad);
        }
    }

    public static double servoGripperreturn = 0.4;
    public static double servoGripper = 0.15;

    private boolean gripped = false;

    public void setGripper(boolean grip) {
        if (grip) {
            gripper.setPosition(servoGripper);
        } else {
            gripper.setPosition(servoGripperreturn);
        }
        gripped = grip;
    }

    public static double servoClamp = 0.8;
    public static double servoClampreturn = 0.5;

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
      if(pixelSensor1.getVoltage() >= pixelSensorvoltage )//|| pixelSensor2.getVoltage() >= pixelSensorvoltage)
           pixelTimer.reset();
        return pixelTimer.milliseconds() > pixelTimeconst;
    }

    ElapsedTime clampTimer = new ElapsedTime();
    double clampTimerconst = 1000;

    void releaseGripper(){
        setGripper(false);
        clampTimer.reset();
    }

    public boolean isPixelLocated = false;

    public void Update() {
        if (pixelDetected()) {
            setGripper(true);
            setClamp(clampTimer.milliseconds() < clampTimerconst && _collector.Lift.isDown());
            _collector.Brush.intakePowerWithDefense(false);

            isPixelLocated = true;

            _lighting.setPower(1);
        } else {
            clampTimer.reset();
            setClamp(!gripped && _collector.Lift.isDown());
            if (inableIntake)
                _collector.Brush.intakePowerWithDefense(true);

            isPixelLocated = false;
            _lighting.setPower(0);
        }

        setperevorotik();

        ToolTelemetry.AddLine(pixelSensor1.getVoltage() + " " + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine(pixelDetected() + "");
    }

    public void PixelCenterGrip(boolean gripped){
        gripper.setPosition(gripped ? PixelCenterOpen : servoGripperreturn);
    }
}


