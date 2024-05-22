package org.woen.team17517.Service.Devices;

import com.qualcomm.robotcore.hardware.AnalogInput;

public class OpticalSensor extends Device<AnalogInput> {
    public OpticalSensor(String name){
        __init(name, AnalogInput.class);
    }
    public double getValue(){
        return device.getVoltage();
    }
}
