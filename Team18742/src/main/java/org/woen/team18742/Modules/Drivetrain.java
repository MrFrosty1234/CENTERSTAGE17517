package org.woen.team18742.Modules;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.OdometrsOdometry;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.Vector2;

@Module
public class Drivetrain implements IRobotModule {
    private DcMotorEx _leftForwardDrive;
    private DcMotorEx _rightForwardDrive;
    private DcMotorEx _leftBackDrive;
    private DcMotorEx _rightBackDrive;
    private Gyroscope _gyro;

    @Override
    public void Init(BaseCollector collector) {
        _leftForwardDrive = Devices.LeftForwardDrive;
        _rightBackDrive = Devices.RightBackDrive;
        _rightForwardDrive = Devices.RightForwardDrive;
        _leftBackDrive = Devices.LeftBackDrive;

        _leftForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _rightForwardDrive.setDirection(REVERSE);
        _rightBackDrive.setDirection(REVERSE);

        _gyro = collector.GetModule(Gyroscope.class);
    }

    @Override
    public void Start() {
        ResetEncoders();
    }

    private void DriveDirection(Vector2 speed, double rotate) {
        _leftForwardDrive.setPower(speed.X - speed.Y + rotate);
        _rightBackDrive.setPower(speed.X - speed.Y - rotate);
        _leftBackDrive.setPower(speed.X + speed.Y + rotate);
         _rightForwardDrive.setPower(speed.X + speed.Y - rotate);
    }

    public void SimpleDriveDirection(Vector2 speed, double rotate){
        if(Configs.DriveTrainWheels.isUsePids)
            SetCMSpeed(Vector2.Multiply(speed, new Vector2(Configs.DriveTrainWheels.MaxSpeedX, Configs.DriveTrainWheels.MaxSpeedY)), rotate * Configs.DriveTrainWheels.MaxSpeedTurn);
        else
            DriveDirection(speed, rotate);
    }

    public void SetCMSpeed(Vector2 speed, double rotate){

    }

    public void ResetEncoders() {
        _leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        _rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double GetLeftBackEncoder() {
        return _leftBackDrive.getCurrentPosition() / Configs.DriveTrainWheels.encoderconstat * PI * Configs.DriveTrainWheels.diameter;
    }

    public double GetLeftForwardEncoder() {
        return _leftForwardDrive.getCurrentPosition() / Configs.DriveTrainWheels.encoderconstat * PI * Configs.DriveTrainWheels.diameter;
    }

    public double GetRightBackEncoder() {
        return _rightBackDrive.getCurrentPosition() / Configs.DriveTrainWheels.encoderconstat * PI * Configs.DriveTrainWheels.diameter;
    }

    public double GetRightForwardEncoder() {
        return _rightForwardDrive.getCurrentPosition() / Configs.DriveTrainWheels.encoderconstat * PI * Configs.DriveTrainWheels.diameter;
    }

    @Override
    public void Stop() {
        _leftForwardDrive.setPower(0);
        _rightBackDrive.setPower(0);
        _leftBackDrive.setPower(0);
        _rightForwardDrive.setPower(0);
    }

    public void SetSpeedWorldCoords(Vector2 speed, double rotate) {
        Vector2 worldSpeed = speed.Turn(-_gyro.GetRadians());

        worldSpeed.Y *= 1d + (1d - Configs.Odometry.YLag);

        SimpleDriveDirection(worldSpeed, rotate);
    }
}
