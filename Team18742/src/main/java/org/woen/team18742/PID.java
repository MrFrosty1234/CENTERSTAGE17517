package org.woen.team18742;

public class PID {
    private final double _pKoef, _dKoef, _iKoef;
    private double _integrall = 0, _errOld = 0;

    public PID(double pKoef, double iKoef, double dKoef) {
        _pKoef = pKoef;
        _iKoef = iKoef;
        _dKoef = dKoef;
    }

    public void Reset(){
        _integrall = 0;
        _errOld = 0;
    }

    public double Update(double val, int desiredVal) {
        double error = val - desiredVal;

        _integrall += error;

        double u = error * _pKoef + _integrall * _iKoef + (error - _errOld) * _dKoef;

        _errOld = error;

        return u;
    }
}
