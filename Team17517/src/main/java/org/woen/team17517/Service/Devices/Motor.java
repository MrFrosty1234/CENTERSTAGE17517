package org.woen.team17517.Service.Devices;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.woen.team17517.Service.PID;
@Config
public class Motor extends Device<DcMotorEx> {
    public Motor(String name) {
        __init(name,DcMotorEx.class);
    }
    enum DIRECTION{
        FORWARD(1),REVERS(-1);
        int k;
        DIRECTION(int k){this.k = k;}
    }
    DIRECTION direction;
    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public double speed(){
        return direction.k*device.getVelocity();
    }
    public double position(){
        return direction.k*device.getCurrentPosition();
    }
    public double current(){
        return direction.k*device.getCurrent(CurrentUnit.AMPS);
    }
    public static double kp;
    public static double ki;
    public static double kd;
    public static double ks;
    public static double maxI;
    PID pid = new PID(kp,ki,kd,ks,maxI,0);
    public void setSpeed(double speed){
        pid.setCoeficent(kp,ki,kd,ks,maxI,0);
        device.setPower(pid.pid(speed,speed())*direction.k);
    }
    public void setPower(double power){
        device.setPower(power);
    }
}
