package org.woen.team18742.Modules;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.PI;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.BulkInit;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.OdometrsOdometry;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.Motor.EncoderControl;
import org.woen.team18742.Tools.Motor.Motor;
import org.woen.team18742.Tools.Motor.ReductorType;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class Drivetrain implements IRobotModule {
    private Motor _leftForwardDrive, _rightForwardDrive, _leftBackDrive, _rightBackDrive;
    private EncoderControl _leftForwardEncoder, _rightForwardEncoder, _leftBackEncoder, _rightBackEncoder;

    private Gyroscope _gyro;

    @Override
    public void Start() {
        ResetEncoders();

        _leftForwardEncoder.Start();
        _rightBackEncoder.Start();
        _rightForwardEncoder.Start();
        _leftBackEncoder.Start();
    }

    @Override
    public void Update() {
        _leftForwardEncoder.Update();
        _rightBackEncoder.Update();
        _rightForwardEncoder.Update();
        _leftBackEncoder.Update();
    }

    @Override
    public void Init(BaseCollector collector) {
        _leftForwardDrive = new Motor(Devices.LeftForwardDrive, ReductorType.TWENTY);
        _rightBackDrive = new Motor(Devices.RightBackDrive, ReductorType.TWENTY);
        _rightForwardDrive = new Motor(Devices.RightForwardDrive, ReductorType.TWENTY);
        _leftBackDrive = new Motor(Devices.LeftBackDrive, ReductorType.TWENTY);

        _leftForwardEncoder = new EncoderControl(Devices.LeftForwardDrive, ReductorType.TWENTY, Configs.DriveTrainWheels.wheelDiameter);
        _rightBackEncoder = new EncoderControl(Devices.RightBackDrive, ReductorType.TWENTY, Configs.DriveTrainWheels.wheelDiameter);
        _rightForwardEncoder = new EncoderControl(Devices.RightForwardDrive, ReductorType.TWENTY, Configs.DriveTrainWheels.wheelDiameter);
        _leftBackEncoder = new EncoderControl(Devices.LeftBackDrive, ReductorType.TWENTY, Configs.DriveTrainWheels.wheelDiameter);

        _leftForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        _rightForwardDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        _leftForwardDrive.setDirection(REVERSE);
        _leftBackDrive.setDirection(REVERSE);

        _gyro = collector.GetModule(Gyroscope.class);
    }

    private void DriveDirection(Vector2 speed, double rotate) {
        _leftForwardDrive.setPower(-speed.X + speed.Y + rotate);
        _rightBackDrive.setPower(-speed.X + speed.Y - rotate);
        _leftBackDrive.setPower(-speed.X - speed.Y + rotate);
        _rightForwardDrive.setPower(-speed.X - speed.Y - rotate);
    }

    private void DriveEncoderDirection(Vector2 speed, double rotate) {
        _leftForwardDrive.setEncoderPower(speed.X - speed.Y + rotate);
        _rightBackDrive.setEncoderPower(speed.X - speed.Y - rotate);
        _leftBackDrive.setEncoderPower(speed.X + speed.Y + rotate);
        _rightForwardDrive.setEncoderPower(speed.X + speed.Y - rotate);
    }

    public void SimpleDriveDirection(Vector2 speed, double rotate){
        DriveDirection(speed, rotate);
    }

    public void SetCMSpeed(Vector2 cmSpeed, double rotate){
        cmSpeed.Y *= 1d / Configs.Odometry.YLag;

    //    cmSpeed = Vector2.Multiply(cmSpeed, new Vector2(0.25));

        DriveEncoderDirection(new Vector2(cmSpeed.X / (PI * Configs.DriveTrainWheels.wheelDiameter) * Configs.DriveTrainWheels.encoderconstat,
                cmSpeed.Y / (PI * Configs.DriveTrainWheels.wheelDiameter) * Configs.DriveTrainWheels.encoderconstat),
                (-rotate * (Configs.DriveTrainWheels.Radius * 2d) / (PI * Configs.DriveTrainWheels.wheelDiameter) * Configs.DriveTrainWheels.encoderconstat) / Configs.Odometry.RotateLag);
    }

    public void ResetEncoders() {
        _leftForwardEncoder.Reset();
        _rightBackEncoder.Reset();
        _rightForwardEncoder.Reset();
        _leftBackEncoder.Reset();
    }

    public double GetLeftBackEncoder() {
        return _leftBackEncoder.GetPosition();
    }

    public double GetLeftForwardEncoder() {
        return _leftForwardEncoder.GetPosition();
    }

    public double GetRightBackEncoder() {
        return _rightBackEncoder.GetPosition();
    }

    public double GetRightForwardEncoder() {
        return _rightForwardEncoder.GetPosition();
    }

    public double GetSpeedLeftBackEncoder() {
        return _leftBackEncoder.GetVelocity();
    }

    public double GetSpeedLeftForwardEncoder() {
        return _leftForwardEncoder.GetVelocity();
    }

    public double GetSpeedRightBackEncoder() {
        return _rightBackEncoder.GetVelocity();
    }

    public double GetSpeedRightForwardEncoder() {
        return _rightForwardEncoder.GetVelocity();
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

        worldSpeed.Y *= 1d / Configs.Odometry.YLag;

        SimpleDriveDirection(worldSpeed, rotate);
    }
}
