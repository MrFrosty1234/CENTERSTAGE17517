package org.woen.team18742.Modules;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.Vector2;

@Module
public class DriverTrain implements IRobotModule {
    private DcMotorEx _leftForwardDrive;
    private DcMotorEx _rightForwardDrive;
    private DcMotorEx _leftBackDrive;
    private DcMotorEx _rightBackDrive;
    private Gyroscope _gyro;

    private final PIDF _leftForwardDrivePidf = new PIDF(Configs.DriverTrainLeftForwardPidf.pCof, Configs.DriverTrainLeftForwardPidf.iCof,
            Configs.DriverTrainLeftForwardPidf.dCof, 0, Configs.DriverTrainLeftForwardPidf.fCof, 1, 1);

    private final PIDF _rightForwardDrivePidf = new PIDF(Configs.DriverTrainRightForwardPidf.pCof, Configs.DriverTrainRightForwardPidf.iCof,
            Configs.DriverTrainRightForwardPidf.dCof, 0, Configs.DriverTrainRightForwardPidf.fCof, 1, 1);

    private final PIDF _leftBackDrivePidf = new PIDF(Configs.DriverTrainLeftBackPidf.pCof, Configs.DriverTrainLeftBackPidf.iCof,
            Configs.DriverTrainLeftBackPidf.dCof, 0, Configs.DriverTrainLeftBackPidf.fCof, 1, 1);

    private final PIDF _rightBackDrivePidf = new PIDF(Configs.DriverTrainRightBackPidf.pCof, Configs.DriverTrainRightBackPidf.iCof,
            Configs.DriverTrainRightBackPidf.dCof, 0, Configs.DriverTrainRightBackPidf.fCof, 1, 1);

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
        ResetIncoder();

        _leftForwardDrivePidf.Start();
        _rightForwardDrivePidf.Start();
        _leftBackDrivePidf.Start();
        _rightBackDrivePidf.Start();
    }

    @Override
    public void Update() {
        _leftForwardDrivePidf.UpdateCoefs(Configs.DriverTrainLeftForwardPidf.pCof, Configs.DriverTrainLeftForwardPidf.iCof,
                Configs.DriverTrainLeftForwardPidf.dCof, 0, Configs.DriverTrainLeftForwardPidf.fCof);
        _rightForwardDrivePidf.UpdateCoefs(Configs.DriverTrainRightForwardPidf.pCof, Configs.DriverTrainRightForwardPidf.iCof,
                Configs.DriverTrainRightForwardPidf.dCof, 0, Configs.DriverTrainRightForwardPidf.fCof);
        _leftBackDrivePidf.UpdateCoefs(Configs.DriverTrainLeftBackPidf.pCof, Configs.DriverTrainLeftBackPidf.iCof,
                Configs.DriverTrainLeftBackPidf.dCof, 0, Configs.DriverTrainLeftBackPidf.fCof);
        _rightBackDrivePidf.UpdateCoefs(Configs.DriverTrainRightBackPidf.pCof, Configs.DriverTrainRightBackPidf.iCof,
                Configs.DriverTrainRightBackPidf.dCof, 0, Configs.DriverTrainRightBackPidf.fCof);

        _leftForwardDrive.setPower(_leftForwardDrivePidf.Update(_leftTargetForwardVelocity - GetLeftForwardVelocity(), _leftTargetForwardVelocity));
        _rightBackDrive.setPower(_rightBackDrivePidf.Update(_rightTargetBackVelocity - GetRightBackVelocity(), _rightTargetBackVelocity));
        _leftBackDrive.setPower(_leftBackDrivePidf.Update(_leftTargetBackVelocity - GetLeftBackVelocity(), _leftTargetBackVelocity));
        _rightForwardDrive.setPower(_rightForwardDrivePidf.Update(_rightTargetForwardVelocity - GetRightForwardVelocity(), _rightTargetForwardVelocity));
    }

    public void DriveDirection(Vector2 speed, double rotate) {
        _leftForwardDrive.setPower(speed.X - speed.Y + rotate);
        _rightBackDrive.setPower(speed.X - speed.Y - rotate);
        _leftBackDrive.setPower(speed.X + speed.Y + rotate);
        _rightForwardDrive.setPower(speed.X + speed.Y - rotate);
    }

    public void SetCMSpeed(Vector2 speed, double rotate){
        _leftTargetForwardVelocity = speed.X - speed.Y + rotate;
        _rightTargetBackVelocity = speed.X - speed.Y - rotate;
        _leftTargetBackVelocity = speed.X + speed.Y + rotate;
        _rightTargetForwardVelocity = speed.X + speed.Y - rotate;
    }

    private double _leftTargetForwardVelocity = 0, _rightTargetBackVelocity = 0, _leftTargetBackVelocity = 0, _rightTargetForwardVelocity = 0;

    public void ResetIncoder() {
        _leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        _rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _leftForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        _rightForwardDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double GetLeftBackIncoder() {
        return _leftBackDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetLeftForwardIncoder() {
        return _leftForwardDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetRightBackIncoder() {
        return _rightBackDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetRightForwardIncoder() {
        return _rightForwardDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetRightForwardVelocity() {
        return _rightForwardDrive.getVelocity() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetLeftForwardVelocity() {
        return _leftForwardDrive.getVelocity() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetRightBackVelocity() {
        return _rightBackDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    public double GetLeftBackVelocity() {
        return _leftBackDrive.getCurrentPosition() / Configs.DriverTrainWheels.encoderconstat * PI * Configs.DriverTrainWheels.diametr;
    }

    @Override
    public void Stop() {
        _leftForwardDrive.setPower(0);
        _rightBackDrive.setPower(0);
        _leftBackDrive.setPower(0);
        _rightForwardDrive.setPower(0);
    }

    public void SetSpeedWorldCoords(Vector2 speed, double rotate) {
        DriveDirection(speed.Turn(_gyro.GetRadians()), rotate);
    }
}
