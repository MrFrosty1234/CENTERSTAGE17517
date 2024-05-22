package org.woen.team17517.Service.Devices;

import com.qualcomm.robotcore.hardware.DigitalChannel;

public class DigitalSensor extends Device<DigitalChannel>{
    public void DigitalChannel(String name){__init(name,DigitalChannel.class);}
    public boolean getValue(){
        return device.getState();
    }

}
