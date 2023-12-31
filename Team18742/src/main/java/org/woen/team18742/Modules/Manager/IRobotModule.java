package org.woen.team18742.Modules.Manager;

import org.woen.team18742.Collectors.BaseCollector;

public interface IRobotModule {
    public void Init(BaseCollector collector);

    public void Start();

    public void Update();

    public void Stop();
}
