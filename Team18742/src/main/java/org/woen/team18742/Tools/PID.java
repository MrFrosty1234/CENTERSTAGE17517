package org.woen.team18742.Tools;

import static java.lang.Math.signum;

public class PID {
    private double _pCoef, _dCoef, _iCoef, _limit;
    private double _integrall = 0;
    private double _errOld = 0;
    public double Err = 0;

    public PID(double pKoef, double iKoef, double dKoef, double limit) {
        _pCoef = pKoef;
        _iCoef = iKoef;
        _dCoef = dKoef;
        _limit = limit;
    }

    public void Reset() {
        _integrall = 0;
        _errOld = 0;
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

        double ingeralErr = _integrall * _iCoef;

        double u = error * _pCoef + ingeralErr + (ingeralErr - _errOld) * _dCoef;

        Err = error;

        _errOld = ingeralErr;

        return u;
    }
}
