package org.woen.team18742.Modules;


import android.annotation.SuppressLint;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Intake {
    private Servo servopere;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
    public static double pixelSensorvoltage = 0.125, PixelCenterOpen = 0;//0.4
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

    public static double servoperevorotnazad = 0.96;
    public static final double servoperevorot = 0.35;

    public void setperevorotik() {
        if (_collector.Lift.isUp()) {
            servopere.setPosition(servoperevorot);
        } else if (_collector.Lift.isAverage()) {
            servopere.setPosition(servoperevorot);
            _normalCoup.reset();
        } else {
            servopere.setPosition(servoperevorotnazad);
        }
    }

    public boolean IsCoupNormal() {
        return _normalCoup.milliseconds() > AverageTime;
    }

    public static long AverageTime = 830;

    public static double servoGripperreturn = 0.4;
    public static double servoGripper = 0.13;
    private ElapsedTime _normalCoup = new ElapsedTime(AverageTime);

    private boolean gripped = false;

    public void setGripper(boolean grip) {
        if (grip) {
            gripper.setPosition(servoGripper);
        } else {
            gripper.setPosition(servoGripperreturn);
        }
        gripped = grip;
        isPixelLocated = grip;

        _lighting.setPower(grip ? 1 : 0);
    }

    public static double servoClamp = 0.9;
    public static double servoClampreturn = 0.42;//0.5

    public void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(servoClamp);
        } else {
            clamp.setPosition(servoClampreturn);
        }
    }


    ElapsedTime pixelTimer = new ElapsedTime();
    double pixelTimeconst = 1000;

    public boolean pixelDetected() {
        if (pixelSensor2.getVoltage() >= pixelSensorvoltage /*&& pixelSensor2.getVoltage() >= pixelSensorvoltage*/)
            pixelTimer.reset();
        return pixelTimer.milliseconds() > pixelTimeconst;
    }

    ElapsedTime clampTimer = new ElapsedTime();
    double clampTimerconst = 800;

    void releaseGripper() {
        setGripper(false);
        clampTimer.reset();
    }

    public boolean isPixelLocated = false;

    private ElapsedTime _brushReversTime = new ElapsedTime(ReversTime);

    public static double ReversTime = 2;

    public void Update() {
        if (pixelDetected()) {
            setGripper(true);
            setClamp(clampTimer.milliseconds() < clampTimerconst && _collector.Lift.isDown());

            _brushReversTime.reset();
        } else {
            clampTimer.reset();
            setClamp(!gripped && _collector.Lift.isDown());
        }

        if (isPixelLocated) {
            if (_brushReversTime.seconds() < ReversTime && _collector.Lift.isDown())
                _collector.Brush.Revers();
            else
                _collector.Brush.Stop();
        }

        setperevorotik();

        ToolTelemetry.AddLine(pixelSensor1.getVoltage() + " " + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine(pixelDetected() + "");
    }

    public void PixelCenterGrip ( boolean gripped){
        gripper.setPosition(gripped ? PixelCenterOpen : servoGripperreturn);
    }
}