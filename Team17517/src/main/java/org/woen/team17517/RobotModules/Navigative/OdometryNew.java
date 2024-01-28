package org.woen.team17517.RobotModules.Navigative;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;


public class OdometryNew implements RobotModule {
    UltRobot robot;
    private double voltage;
    private Vector2D vector = new Vector2D();
    private double h;
    public OdometryNew(UltRobot robot){
        this.robot = robot;

        left_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_front_drive");
        odometrLeft =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "odometrLeft");

        right_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_front_drive");
        right_back_drive =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_back_drive");

        voltage = 12;

        reset();

    }
    private void reset()
    {
        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        odometrLeft.setDirection(DcMotor.Direction.FORWARD);

        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.REVERSE);

        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        odometrLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odometrLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        vector.setCord(0,0);
        h = 0;
    }
    private  double yEnc;
    private double xEnc;
    private void  encUpdate()
    {
        this.yEnc = (odometrLeft.getCurrentPosition()+ 0*left_front_drive.getCurrentPosition()+
                right_front_drive.getCurrentPosition()*0+ right_back_drive.getCurrentPosition())/2.0;
        this.xEnc = ((-odometrLeft.getCurrentPosition()+ left_front_drive.getCurrentPosition()-
                0*right_front_drive.getCurrentPosition()+ 0*right_back_drive.getCurrentPosition())/2.0)*kSlide;
        h = robot.gyro.getAngle();
    }

    private double startTime = System.currentTimeMillis();
    private void odometerUpdate(){
        this.yEnc = (left_front_drive.getCurrentPosition() + odometrLeft.getCurrentPosition())/2d;
        this.xEnc = right_back_drive.getCurrentPosition();
        if (true){
            h = robot.gyro.getAngle();
            startTime = System.currentTimeMillis();
        }else {
            h = (left_front_drive.getCurrentPosition()-right_front_drive.getCurrentPosition())/2d;
        }
    }

    private double kSlide = 1;
    private final DcMotorEx left_front_drive;
    private final DcMotorEx odometrLeft;
    private final DcMotorEx right_front_drive;
    private final DcMotorEx right_back_drive;
    public double getX(){
        return vector.getX();
    }
    public double getY(){
        return vector.getY();
    }
    public double getH(){
        return h;
    }
    public Vector2D getPositionVector(){return vector;}
    private Vector2D vectorCleanPosition = new Vector2D();
    public void update(){
        odometerUpdate();
        vectorCleanPosition.setCord(xEnc,yEnc);
        vectorCleanPosition.vectorRat(h);
        vector = Vector2D.vectorSum(vector, vectorCleanPosition);
    }
}
