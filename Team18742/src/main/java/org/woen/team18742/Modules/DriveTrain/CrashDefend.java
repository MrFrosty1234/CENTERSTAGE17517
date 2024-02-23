package org.woen.team18742.Modules.DriveTrain;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Color;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class CrashDefend implements IRobotModule {
    private OdometryHandler _odometry;
    private Gyroscope _gyro;
    private Drivetrain _driveTrain;

    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(OdometryHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driveTrain = collector.GetModule(Drivetrain.class);
    }

    @Override
    public void Update() {
        ToolTelemetry.DrawPolygon(new Vector2[]{
                Vector2.Plus(_odometry.Position,
                        new Vector2(Configs.DriveTrainWheels.WheelsRadius, Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(Configs.DriveTrainWheels.WheelsRadius, -Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(-Configs.DriveTrainWheels.WheelsRadius, -Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(-Configs.DriveTrainWheels.WheelsRadius, Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians()))
        }, Color.BLUE);
    }
}
