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
import org.woen.team18742.Tools.Motor.Motor;
import org.woen.team18742.Tools.Motor.ReductorType;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class Drivetrain implements IRobotModule {
    private static Motor _leftForwardDrive;
    private static Motor _rightForwardDrive;
    private static Motor _leftBackDrive;
    private static Motor _rightBackDrive;
    private Gyroscope _gyro;

    @BulkInit
    public static void BulkInit(){
        _leftForwardDrive = new Motor(Devices.LeftForwardDrive, ReductorType.TWENTY);
        _rightBackDrive = new Motor(Devices.RightBackDrive, ReductorType.TWENTY);
        _rightForwardDrive = new Motor(Devices.RightForwardDrive, ReductorType.TWENTY);
        _leftBackDrive = new Motor(Devices.LeftBackDrive, ReductorType.TWENTY);

        ResetEncoders();
    }

    @Override
    public void Init(BaseCollector collector) {
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

        DriveEncoderDirection(new Vector2(cmSpeed.X / (PI * Configs.DriveTrainWheels.diameter) * Configs.DriveTrainWheels.encoderconstat,
                cmSpeed.Y / (PI * Configs.DriveTrainWheels.diameter) * Configs.DriveTrainWheels.encoderconstat), -rotate * Configs.DriveTrainWheels.Radius / (PI * Configs.DriveTrainWheels.diameter) * Configs.DriveTrainWheels.encoderconstat / 4);
    }

    public static void ResetEncoders() {
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

        worldSpeed.Y *= 1d / Configs.Odometry.YLag;

        SimpleDriveDirection(worldSpeed, rotate);
    }
}
