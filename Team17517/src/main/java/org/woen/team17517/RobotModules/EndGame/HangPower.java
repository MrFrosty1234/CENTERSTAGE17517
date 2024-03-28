package org.woen.team17517.RobotModules.EndGame;

public enum HangPower {
    UP, DOWN, ZERO;
    public static final double powerToHangUp = -1;
    public static final double powerToHangDown = 1;
    public static double powerToRide = 0;
    public double get(){
        switch (this){
            case UP:
                return powerToHangUp;
            case DOWN:
                return powerToHangDown;
            case ZERO:
                return powerToRide;
            default:
                return 0;
        }
    }
}
