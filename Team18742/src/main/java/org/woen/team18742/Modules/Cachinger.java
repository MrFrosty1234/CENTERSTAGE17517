package org.woen.team18742.Modules;

import com.qualcomm.hardware.lynx.LynxModule;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;

import java.util.List;

@Module
public class Cachinger implements IRobotModule {
    private List<LynxModule> _hubs;

    @Override
    public void Init(BaseCollector collector){
        _hubs = Devices.Hubs;

        for(LynxModule i: _hubs)
            i.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
    }

    @Override
    public void Start() {}

    @Override
    public void Update(){
        if(Configs.GeneralSettings.IsCachinger.Get())
            for(LynxModule i: _hubs)
                i.getBulkData();
    }

    @Override
    public void Stop() {}
}
