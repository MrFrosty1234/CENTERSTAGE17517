package org.woen.team18742.Tools;

import static java.lang.Math.signum;

public class PID {
    private double _pCoef;
    private double _dCoef;
    private double _iCoef;
    private final double _limit;
    private double _integrall = 0;
    private double _errOld = 0;
    public double Err = 0;

    private double _limitU = 0;

    public PID(double pKoef, double iKoef, double dKoef, double limitU, double limit){
        this(pKoef, iKoef, dKoef, limit);

        _limitU = limitU;
    }

    public PID(double pKoef, double iKoef, double dKoef, double limit) {
        _pCoef = pKoef;
        _iCoef = iKoef;
        _dCoef = dKoef;
        _limit = limit;

        _limitU = 1;
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

        double u = error * _pCoef + _integrall * _iCoef + (error - _errOld) * _dCoef;

        Err = error;

        _errOld = error;

        if(Math.abs(u) > _limitU){
            return _limitU * Math.signum(u);
        }
        return u;
    }
}
