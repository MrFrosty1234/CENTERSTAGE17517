package org.woen.team18742.Tools.Configs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.ValueProvider;

public class DashboardDouble {
    public DashboardDouble(String category, String name, double value) {
        _key = category + name;

        _value = DataManager.ReadDouble(_key, value);

        FtcDashboard.getInstance().addConfigVariable(category, name, new Provider());
    }

    private double _value;
    private final String _key;

    public double Get() {
        return _value;
    }

    public void NotSaveSet(double value){
        _value = value;
    }

    private class Provider implements ValueProvider<Double> {
        @Override
        public Double get() {
            return _value;
        }

        @Override
        public void set(Double value) {
            _value = value;

            DataManager.AddDouble(_key, _value);
        }
    }
}
