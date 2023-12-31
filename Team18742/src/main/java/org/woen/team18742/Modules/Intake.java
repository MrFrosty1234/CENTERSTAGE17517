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
import org.woen.team18742.Modules.Lift.Lift;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Intake implements IRobotModule {
    private Servo servopere;
    private Servo gripper; // Штучка которая хватает пиксели в подъемнике
    private Servo clamp; // Сервак который прижимает пиксели после щеток
    private AnalogInput pixelSensor1, pixelSensor2; // Датчик присутствия пикселей над прижимом
    private DcMotor _lighting;

    private Brush _brush;
    private Lift _lift;

    @Override
    public void Init(BaseCollector collector) {
        pixelSensor1 = Devices.PixelSensor1;
        pixelSensor2 = Devices.PixelSensor2;
        gripper = Devices.Gripper;
        clamp = Devices.Clamp;
        servopere = Devices.Servopere;
        _lighting = Devices.LightingMotor;

        _brush = (Brush) collector.GetModule(Brush.class);
        _lift = (Lift) collector.GetModule(Lift.class);
    }

    @Override
    public void Start() {}

    public void setperevorotik() {
        if (_lift.isUp()) {
            servopere.setPosition(Configs.Intake.servoperevorot);
        } else if (_lift.isAverage()) {
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

    @Override
    public void Update() {
        if (pixelDetected()) {
            setGripper(true);
            setClamp(clampTimer.milliseconds() < clampTimerconst && _lift.isDown());

            _brushReversTime.reset();
        } else {
            clampTimer.reset();
            setClamp(!gripped && _lift.isDown());
        }

        if (isPixelLocated) {
            if (_brushReversTime.milliseconds() < Configs.Intake.ReversTime)
                _brush.Revers();
            else
                _brush.Stop();
        }

        setperevorotik();

        ToolTelemetry.AddLine(pixelSensor1.getVoltage() + " " + pixelSensor2.getVoltage());
        ToolTelemetry.AddLine(pixelDetected() + "");
    }

    @Override
    public void Stop() {}

    public void PixelCenterGrip ( boolean gripped){
        gripper.setPosition(gripped ? Configs.Intake.PixelCenterOpen : Configs.Intake.servoGripperreturn);
    }
}