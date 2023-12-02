package org.woen.team18742.Tools;

import static java.lang.Math.signum;

public class PID {
    private final double _pKoef, _dKoef, _iKoef, _limit;
    private double _integrall = 0;
    public double ErrOld = 0;
    public double Err = 0;

    public PID(double pKoef, double iKoef, double dKoef, double limit) {
        _pKoef = pKoef;
        _iKoef = iKoef;
        _dKoef = dKoef;
        _limit = limit;
    }

    public void Reset() {
        _integrall = 0;
        ErrOld = 0;
        Err = 0;
    }

    public double Update(double sensor, double target) {
        return Update(target);
    }

    public double Update(double error) {
        _integrall += error;

        if(Math.abs(_integrall) >= _limit)
            _integrall = signum(_integrall) * _limit;

        double u = error * _pKoef + _integrall * _iKoef + (error - ErrOld) * _dKoef;

        Err = error;

        ErrOld = error;

        return u;
    }
}
