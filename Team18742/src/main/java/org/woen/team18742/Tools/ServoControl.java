package org.woen.team18742.Tools;

import com.qualcomm.robotcore.hardware.Servo;

public class ServoControl {
    Servo servo;
   private double servoRangeDeg = 180;
    private double servoSekDeg = 180;
    private double targetDegree = 90;
    private double nowDegree = 90;
    public  ServoControl(Servo servo, double sDr,double sdk){

        this.servo = servo;
        servoRangeDeg = sDr;
        servoSekDeg = 60 /sdk;
    }
    public void setPositionDegres(double deg){
        deg = deg /servoRangeDeg;
        servo.setPosition(deg);
    }
    public double getTargetDegree(){
        return targetDegree;
    }

   /// public double getNowDegree() {
        // nowDegree += zn*sp*time и нужна функция для получ инф достиг ли сервак своей позиции
}
