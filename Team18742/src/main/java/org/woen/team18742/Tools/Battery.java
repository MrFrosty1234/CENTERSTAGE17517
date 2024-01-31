package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Configs.Configs;

public class Battery {
    public static double ChargeDelta = 1;
    public static double Voltage = 1;

    private VoltageSensor _voltageSensor;

    public Battery(BaseCollector collector){
        _voltageSensor = Devices.VoltageSensor;
    }

    public void Update(){
        Voltage = _voltageSensor.getVoltage();
        ChargeDelta = Voltage / Configs.Battery.CorrectCharge;
    }
}
