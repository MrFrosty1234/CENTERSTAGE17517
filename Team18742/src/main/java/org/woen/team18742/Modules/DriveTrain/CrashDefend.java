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

    private Square _square;

    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(OdometryHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driveTrain = collector.GetModule(Drivetrain.class);
    }

    @Override
    public void Update() {
        _square = new Square(
                Vector2.Plus(_odometry.Position,
                        new Vector2(Configs.DriveTrainWheels.WheelsRadius, Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(Configs.DriveTrainWheels.WheelsRadius, -Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(-Configs.DriveTrainWheels.WheelsRadius, -Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians())),
                Vector2.Plus(_odometry.Position,
                        new Vector2(-Configs.DriveTrainWheels.WheelsRadius, Configs.DriveTrainWheels.WheelsRadius).Turn(_gyro.GetRadians()))
        );

        Triangle[] triangles = _square.GetTriangles();


        ToolTelemetry.DrawPolygon(_square.GetPoints(), Color.BLUE);
    }

    private class Triangle {
        public Vector2 p1, p2, p3;

        public Triangle(Vector2 p1, Vector2 p2, Vector2 p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }
    }

    private class Square {
        public Vector2 p1, p2, p3, p4;

        public Square(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            this.p4 = p4;
        }

        public Vector2[] GetPoints() {
            return new Vector2[]{
                    p1,
                    p2,
                    p3,
                    p4
            };
        }

        public Triangle[] GetTriangles() {
            return new Triangle[]{
                    new Triangle(_square.p1, _square.p2, _square.p3),
                    new Triangle(_square.p3, _square.p4, _square.p1)
            };
        }
    }
}
