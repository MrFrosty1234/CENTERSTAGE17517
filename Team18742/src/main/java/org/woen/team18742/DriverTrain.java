package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriverTrain {
    private DcMotor _leftForwardDrive = null;
    private DcMotor _rightForwardDrive = null;
    private DcMotor _leftBackDrive = null;
    private DcMotor _rightBackDrive = null;

    private final double diametr = 9.8, encoderconstat = 1440;

    public DriverTrain(BaseCollector collector) {
        _leftForwardDrive = collector.CommandCode.hardwareMap.get(DcMotor.class, "leftmotor");
        _rightForwardDrive = collector.CommandCode.hardwareMap.get(DcMotor.class, "rightmotor");
        _leftBackDrive = collector.CommandCode.hardwareMap.get(DcMotor.class, "leftbackmotor");
        _rightBackDrive = collector.CommandCode.hardwareMap.get(DcMotor.class, "rightbackmotor");
        _leftForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightForwardDrive.setDirection(REVERSE);
        _rightBackDrive .setDirection(REVERSE);

    }

    public void DriveDirection(double forward, double side, double rotate){
       _leftForwardDrive.setPower(forward + side + rotate);
       _rightBackDrive.setPower(forward + side - rotate);
        _leftBackDrive.setPower(forward - side + rotate);
        _rightForwardDrive.setPower(forward - side - rotate);
    }

    public void ResetIncoder() {
        _leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double GetForwardDistance(){
        return (_leftBackDrive.getCurrentPosition()-_rightForwardDrive.getCurrentPosition()+_leftForwardDrive.getCurrentPosition()-_rightBackDrive.getCurrentPosition())/4.0/encoderconstat*PI*diametr;
    }

    public double GetSideDistance(){
        return (_leftBackDrive.getCurrentPosition()+_rightForwardDrive.getCurrentPosition()+_leftForwardDrive.getCurrentPosition()+_rightBackDrive.getCurrentPosition())/4.0/encoderconstat*PI*diametr;
    }

    public double GetLeftBackIncoder(){
        return  _leftBackDrive.getCurrentPosition() / encoderconstat * PI * diametr;
    }

    public double GetLeftForwardIncoder(){
        return  _leftForwardDrive.getCurrentPosition() / encoderconstat * PI * diametr;
    }

    public double GetRightBackIncoder(){
        return  _rightBackDrive.getCurrentPosition() / encoderconstat * PI * diametr;
    }

    public double GetRightForwardIncoder(){
        return  _rightForwardDrive.getCurrentPosition() / encoderconstat * PI * diametr;
    }

    public void Stop(){
        _leftForwardDrive.setPower(0);
        _rightBackDrive.setPower(0);
        _leftBackDrive.setPower(0);
        _rightForwardDrive.setPower(0);
    }

}
