package org.woen.team18742.Modules.Odometry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;

@Module
public class CVOdometry implements IRobotModule {
    private AprilTagProcessor  _aprilTagProcessor = null;

    public Vector2 Position = new Vector2(), Speed = new Vector2(), OldPosition = new Vector2();
    public boolean IsZero = true;

    private Vector2 _cameraPosition = new Vector2(Configs.Camera.CameraX, Configs.Camera.CameraY);
    private Gyroscope _gyro;

    private final ElapsedTime _deltaTime = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector){
        _gyro = collector.GetModule(Gyroscope.class);
    }

    public VisionProcessor GetProcessor(){
        _aprilTagProcessor = new AprilTagProcessor.Builder().setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES).setDrawAxes(true).build();

        return _aprilTagProcessor;
    }

    @Override
    public void Start() {
        _deltaTime.reset();

        Position = AutonomCollector.StartPosition.Position.copy();
        OldPosition = AutonomCollector.StartPosition.Position.copy();
    }

    @Override
    public void Update() {
        _cameraPosition.X = Configs.Camera.CameraX;
        _cameraPosition.Y = Configs.Camera.CameraY;

        ArrayList<AprilTagDetection> detections = _aprilTagProcessor.getDetections();

        double xSum = 0, ySum = 0;

        int suitableDetections = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.rawPose != null && detection.decisionMargin > Configs.Camera.CameraAccuracy) {
                // Считать позицию тэга относительно камеры и записать её в VectorF
                AprilTagPoseRaw rawTagPose = detection.rawPose;
                VectorF rawTagPoseVector = new VectorF(
                        (float) rawTagPose.x, (float) rawTagPose.y, (float) rawTagPose.z);
                // Считать вращение тега относительно камеры
                MatrixF rawTagRotation = rawTagPose.R;
                // Cчитать метаданные из тэга
                AprilTagMetadata metadata = detection.metadata;
                // Достать позицию тега относительно поля
                VectorF fieldTagPos = metadata.fieldPosition.multiplied((float) DistanceUnit.mmPerInch / 10f);
                // Достать угол тега относительно поля
                Quaternion fieldTagQ = metadata.fieldOrientation;
                // Повернуть вектор относительного положения на угол между камерой и тегом
                rawTagPoseVector = rawTagRotation.inverted().multiplied(rawTagPoseVector);
                // Повернуть относительное положение на угол между тегом и полем
                VectorF rotatedPosVector = fieldTagQ.applyToVector(rawTagPoseVector);
                // Вычесть полученное положение камеры из абсолютного положения тега
                VectorF fieldCameraPos = fieldTagPos.subtracted(rotatedPosVector);

                xSum += fieldCameraPos.get(0);
                ySum += fieldCameraPos.get(1);

                suitableDetections++;
            }
        }

        if(suitableDetections == 0){
            IsZero = true;

            return;
        }

        IsZero = false;

        Position.X = xSum / suitableDetections;
        Position.Y = -ySum / suitableDetections;

        Position = Vector2.Minus(Position, _cameraPosition.Turn(_gyro.GetRadians()));

        Vector2 DeltaPosition = Vector2.Minus(OldPosition, Position);

        Speed.X = DeltaPosition.X / _deltaTime.seconds();
        Speed.Y = DeltaPosition.Y / _deltaTime.seconds();

        ToolTelemetry.AddLine("CVOdometry = " + Position);
        ToolTelemetry.DrawCircle(Position, 10, "#FFFFFF");

        OldPosition = Position;
        _deltaTime.reset();
    }
}