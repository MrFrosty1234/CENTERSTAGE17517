package org.woen.team18742.Modules;

import com.qualcomm.hardware.lynx.LynxModule;

import org.woen.team18742.Tools.Devices;

import java.util.List;

public class Cachinger {
    List<LynxModule> _hubs;

    public Cachinger(){
        _hubs = Devices.Hubs;

        for(LynxModule i: _hubs)
            i.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }

    public void Update(){
        for(LynxModule i: _hubs)
            i.getBulkData();
    }
}
