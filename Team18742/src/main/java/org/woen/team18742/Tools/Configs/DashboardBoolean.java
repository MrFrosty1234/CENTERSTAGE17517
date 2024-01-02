package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;

public class DashboardBoolean {
    public DashboardBoolean(String category, String name, boolean value) {
        _key = category + name;

        _value = DataManager.ReadBoolean(_key, value);

        FtcDashboard.getInstance().addConfigVariable(category, name, new Provider());
    }

    private boolean _value;
    private final String _key;

    public boolean Get() {
        return _value;
    }

    public void NotSaveSet(boolean value){
        _value = value;
    }

    private class Provider implements ValueProvider<Boolean> {
        @Override
        public Boolean get() {
            return _value;
        }

        @Override
        public void set(Boolean value) {
            _value = value;

            DataManager.AddBoolean(_key, _value);
        }
    }
}
