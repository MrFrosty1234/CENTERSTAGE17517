package org.woen.team18742.Tools;

import com.qualcomm.robotcore.util.ElapsedTime;

public class ExponationFilter {
    private double _coef;
    private ElapsedTime _time = new ElapsedTime();

    public ExponationFilter(double coef){
        _coef = coef;
    }

    public void Reset(){
        _time.reset();
    }

    public void UpdateCoef(double coef){
        _coef = coef;
    }

    public double UpdateRaw(double val, double delta){
        double result = val + delta * (_time.seconds() / (_coef + _time.seconds()));

        _time.reset();

        return result;
    }

    public double Update(double val1, double val2){
        return UpdateRaw(val2 - val1, val1);
    }
}
