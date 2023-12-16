package org.woen.team18742.Modules.Odometry;

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
import org.woen.team18742.Modules.Camera.Camera;
import org.woen.team18742.Tools.ToolTelemetry;

import java.util.ArrayList;

public class CVOdometry {
    private AprilTagProcessor  _aprilTagProcessor = null;

    public double X, Y;
    public boolean IsZero = true;

    public void Start(){
        Update();
    }

    public VisionProcessor GetProcessor(){
        _aprilTagProcessor = new AprilTagProcessor.Builder().setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES).setDrawAxes(true).build();

        return _aprilTagProcessor;
    }

    public void Update() {
        ArrayList<AprilTagDetection> detections = _aprilTagProcessor.getDetections();

        if(detections.size() == 0){
            X = 0;
            Y = 0;

            IsZero = true;

            return;
        }

        IsZero = false;

        double xSum = 0, ySum = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.rawPose != null) {
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
            }
        }

        X = xSum / detections.size();
        Y = -ySum / detections.size();

        ToolTelemetry.AddLine("CVOdometry = " + X + " " + Y);
        ToolTelemetry.DrawCircle(X, Y, 10, "#FFFFFF");
    }
}