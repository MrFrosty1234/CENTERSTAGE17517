package org.woen.team18742.Modules.Odometry;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

import java.util.ArrayList;

@Config
public class CVOdometry {
    private AprilTagProcessor  _aprilTagProcessor = null;

    public Vector2 Position = new Vector2();
    public boolean IsZero = true;

    public static double CameraX = 16.01, CameraY = 16.18;

    private Vector2 _cameraPosition = new Vector2(CameraX, CameraY);
    private final Gyroscope _gyro;

    public static float Accuracy = 130;

    public CVOdometry(BaseCollector collector){
        _gyro = collector.Gyro;
    }

    public VisionProcessor GetProcessor(){
        _aprilTagProcessor = new AprilTagProcessor.Builder().setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES).setDrawAxes(true).build();

        return _aprilTagProcessor;
    }

    public void Update() {
        _cameraPosition.X = CameraX;
        _cameraPosition.Y = CameraY;

        ArrayList<AprilTagDetection> detections = _aprilTagProcessor.getDetections();

        double xSum = 0, ySum = 0;

        int suitableDetections = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.rawPose != null && detection.decisionMargin > Accuracy) {
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

        ToolTelemetry.AddLine("CVOdometry = " + Position);
        ToolTelemetry.DrawCircle(Position, 10, "#FFFFFF");
    }
}