package org.woen.team18742.Modules.Manager;

import org.woen.team18742.Collectors.BaseCollector;

public interface IRobotModule {
    public default void Init(BaseCollector collector){}

    public default void Start(){}

    public default void Update(){}

    public default void Stop(){}
}
