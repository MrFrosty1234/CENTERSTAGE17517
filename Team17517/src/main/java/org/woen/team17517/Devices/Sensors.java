package org.woen.team17517.Devices;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Sensors{
    HardwareMap hardwareMap;
    public Sensors(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
        buttonUp        = hardwareMap.digitalChannel.get("buttonUp");

        buttonDown      = hardwareMap.digitalChannel.get("buttonDown");

        upPixelsSensor  = hardwareMap.get(AnalogInput.class,"upPixelsSensor");

        downPixelSensor = hardwareMap.get(AnalogInput.class,"downPixelsSensor");

        reset();
    }
    public DigitalChannel buttonUp;
    public DigitalChannel buttonDown;
    public AnalogInput upPixelsSensor;
    public AnalogInput downPixelSensor;

    private void reset(){
        buttonUp.setMode(DigitalChannel.Mode.INPUT);
        buttonDown.setMode(DigitalChannel.Mode.INPUT);
    }
}

