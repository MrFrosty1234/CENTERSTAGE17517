package org.woen.team17517.Service.Devices;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;


public abstract class Device<T>{
    public void setHardwareMap(HardwareMap hardwareMap){
        Device.hardwareMap = hardwareMap;
    }
    protected String name;
    public static HardwareMap hardwareMap;
    protected Class<T> classDev;
    protected T device;
    SortedSet<String> allDeviceNameList;
    List<HardwareDevice> devList = new ArrayList<>();
    protected void __init(String deviceName,Class<T> deviceClass){
        this.name = deviceName;
        this.classDev = deviceClass;
    }
    public Class<T> getClassDev() {
        return classDev;
    }
    public String getName() {
        return name;
    }

}