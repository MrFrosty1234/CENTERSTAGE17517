package org.woen.team18742;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriverTrain {
    private DcMotor _leftForwardDrive = null;
    private DcMotor _rightForwardDrive = null;
    private DcMotor _grabberDrive = null;
    private DcMotor _leftBackDrive = null;
    private DcMotor _rightBackDrive = null;
    private Collector _collector = null;

    private final double diametr = 9.8, encoderconstat = 1440;

    public DriverTrain(Collector collector) {
        _collector = collector;

        _leftForwardDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "leftmotor");
        _rightForwardDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "rightmotor");
        _grabberDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "grabbermotor");
        _leftBackDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "leftbackmotor");
        _rightBackDrive = _collector.CommandCode.hardwareMap.get(DcMotor.class, "rightbackmotor");

        _leftBackDrive.setDirection(REVERSE);
        _leftForwardDrive.setDirection(REVERSE);
    }

    public void Update() {

    }

    public void DriveDirection(double forward, double side, double rotate){
        _leftForwardDrive.setPower(forward - side - rotate);
        _rightBackDrive.setPower(forward - side + rotate);
        _leftBackDrive.setPower(forward + side - rotate);
        _rightForwardDrive.setPower(forward + side + rotate);
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

    public double GetDistance(){
        return (_leftBackDrive.getCurrentPosition()+_rightForwardDrive.getCurrentPosition()+_leftForwardDrive.getCurrentPosition()+_rightBackDrive.getCurrentPosition())/4.0/encoderconstat*PI*diametr;
    }
}
