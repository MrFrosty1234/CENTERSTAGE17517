package org.woen.team17517.RobotModules.Grabber;

public enum BrushMode {
    IN(0.8),OUT(-0.5),OFF(0);
    double value;
    BrushMode(double value){this.value = value;}
}
