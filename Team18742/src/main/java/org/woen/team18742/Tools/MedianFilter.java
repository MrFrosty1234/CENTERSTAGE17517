package org.woen.team18742.Tools;

import com.qualcomm.robotcore.util.ElapsedTime;

public class MedianFilter {
    private double _coef;
    private ElapsedTime _time = new ElapsedTime();

    public MedianFilter(double coef){
        _coef = coef;
    }

    public void Reset(){
        _time.reset();
    }

    public void UpdateCoef(double coef){
        _coef = coef;
    }

    public double Update(double val1, double val2){
        double result = val1 + (val2 - val1) * (_time.seconds() / (_coef + _time.seconds()));

        _time.reset();

        return result;
    }
}
