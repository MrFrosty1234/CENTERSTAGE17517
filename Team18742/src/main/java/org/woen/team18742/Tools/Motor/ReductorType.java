package org.woen.team18742.Tools.Motor;

public enum ReductorType {
    SIXTY(1440),
    TWENTY(72);

    private ReductorType(int ticks){
        Ticks = ticks;
    }

    public int Ticks;
}
