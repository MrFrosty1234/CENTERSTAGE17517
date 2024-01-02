package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;

public class DashboardInt{
    public DashboardInt(String category, String name, int value) {
        _key = category + name;

        _value = DataManager.ReadInt(_key, value);

        FtcDashboard.getInstance().addConfigVariable(category, name, new Provider());
    }

    private int _value;
    private final String _key;

    public int Get() {
        return _value;
    }

    public void NotSaveSet(int value){
        _value = value;
    }

    private class Provider implements ValueProvider<Integer> {
        @Override
        public Integer get() {
            return _value;
        }

        @Override
        public void set(Integer value) {
            _value = value;

            DataManager.AddInt(_key, _value);
        }
    }
}
