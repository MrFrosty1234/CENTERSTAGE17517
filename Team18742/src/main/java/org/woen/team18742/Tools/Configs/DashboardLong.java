package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;

public class DashboardLong{
    public DashboardLong(String category, String name, long value) {
        _key = category + name;

        _value = DataManager.ReadLong(_key, value);

        FtcDashboard.getInstance().addConfigVariable(category, name, new Provider());
    }

    private long _value;
    private final String _key;

    public long Get() {
        return _value;
    }

    public void NotSaveSet(long value){
        _value = value;
    }

    private class Provider implements ValueProvider<Long> {
        @Override
        public Long get() {
            return _value;
        }

        @Override
        public void set(Long value) {
            _value = value;

            DataManager.AddLong(_key, _value);
        }
    }
}
