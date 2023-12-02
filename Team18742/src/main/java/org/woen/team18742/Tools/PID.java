package org.woen.team18742.Tools;

import static java.lang.Math.signum;

public class PID {
    private double _pCoef, _dCoef, _iCoef, _limit;
    private double _integrall = 0;
    public double ErrOld = 0;
    public double Err = 0;

    public PID(double pKoef, double iKoef, double dKoef, double limit) {
        _pCoef = pKoef;
        _iCoef = iKoef;
        _dCoef = dKoef;
        _limit = limit;
    }

    public void Reset() {
        _integrall = 0;
        ErrOld = 0;
        Err = 0;
    }
    
    public void UpdateCoefs(double pCoef, double iCoef, double dCoef){
        _pCoef = pCoef;
        _iCoef = iCoef;
        _dCoef = dCoef;
    }

    public double Update(double sensor, double target) {
        return Update(sensor - target);
    }

    public double Update(double error) {
        _integrall += error;

        if(Math.abs(_integrall) >= _limit)
            _integrall = signum(_integrall) * _limit;

        double u = error * _pCoef + _integrall * _iCoef + (error - ErrOld) * _dCoef;

        Err = error;

        ErrOld = error;

        return u;
    }
}
