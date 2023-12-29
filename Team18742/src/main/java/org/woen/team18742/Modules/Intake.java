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
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

public class Intake {
    private Servo servopere;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private BaseCollector _collector; // Штука в которой хранится всё остальное
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

    public void setperevorotik() {
        if (_collector.Lift.isUp()) {
            servopere.setPosition(Configs.Intake.servoperevorot);
        } else if (_collector.Lift.isAverage()) {
            servopere.setPosition(Configs.Intake.servoperevorot);
            _normalCoup.reset();
        } else {
            servopere.setPosition(Configs.Intake.servoperevorotnazad);
        }
    }

    public boolean IsCoupNormal() {
        return _normalCoup.milliseconds() > Configs.Intake.AverageTime;
    }

    private ElapsedTime _normalCoup = new ElapsedTime(Configs.Intake.AverageTime);

    private boolean gripped = false;

    public void setGripper(boolean grip) {
        if (grip) {
            gripper.setPosition(Configs.Intake.servoGripper);
        } else {
            gripper.setPosition(Configs.Intake.servoGripperreturn);
        }
        gripped = grip;
        isPixelLocated = grip;

        _lighting.setPower(grip ? 1 : 0);
    }

    public void setClamp(boolean clampIk) {
        if (clampIk) {
            clamp.setPosition(Configs.Intake.servoClamp);
        } else {
            clamp.setPosition(Configs.Intake.servoClampreturn);
        }
    }


    ElapsedTime pixelTimer = new ElapsedTime();
    double pixelTimeconst = 1000;

    public boolean pixelDetected() {
        if (pixelSensor2.getVoltage() >= Configs.Intake.pixelSensorvoltage /*&& pixelSensor2.getVoltage() >= pixelSensorvoltage*/)
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

    private ElapsedTime _brushReversTime = new ElapsedTime(Configs.Intake.ReversTime);

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
            if (_brushReversTime.milliseconds() < Configs.Intake.ReversTime)
                _collector.Brush.Revers();
            else
                _collector.Brush.Stop();
        }

        setperevorotik();

        ToolTelemetry.AddLine(pixelSensor1.getVoltage() + " " + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine(pixelDetected() + "");
    }

    public void PixelCenterGrip ( boolean gripped){
        gripper.setPosition(gripped ? Configs.Intake.PixelCenterOpen : Configs.Intake.servoGripperreturn);
    }
}