package org.woen.team18742.OpenCV;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.team18742.Odometry;

public class CVOdometry {
    private AprilTagProcessor _aprilTagProcessor;
    private VisionPortal _visionPortal;

    private Odometry _odometry;

    public CVOdometry(Odometry odometry, WebcamName webcamName) {
        _aprilTagProcessor = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .setDrawAxes(true)
                .build();

        _visionPortal = new VisionPortal.Builder()
                .setCamera(webcamName)
                .addProcessor(_aprilTagProcessor)
                .build();

        _visionPortal.setProcessorEnabled(_aprilTagProcessor, false);

        _odometry = odometry;
    }

    public void Start() {
        _visionPortal.setProcessorEnabled(_aprilTagProcessor, true);
    }

    public void Update() {
        double xSum = 0, ySum = 0;

        for (AprilTagDetection detection : _aprilTagProcessor.getDetections()) {
            if (detection.rawPose != null) {
                /* Считать позицию тэга относительно камеры и записать её в VectorF*/
                AprilTagPoseRaw rawTagPose = detection.rawPose;
                VectorF rawTagPoseVector = new VectorF(
                        (float) rawTagPose.x, (float) rawTagPose.y, (float) rawTagPose.z);
                /* Считать вращение тега относительно камеры */
                MatrixF rawTagRotation = rawTagPose.R;
                /* Cчитать метаданные из тэга */
                AprilTagMetadata metadata = detection.metadata;
                /* Достать позицию тега относительно поля */
                VectorF fieldTagPos = metadata.fieldPosition.multiplied((float) DistanceUnit.mmPerInch / 10f);
                /* Достать угол тега относительно поля */
                Quaternion fieldTagQ = metadata.fieldOrientation;
                /* Повернуть вектор относительного положения на угол между камерой и тегом */
                rawTagPoseVector = rawTagRotation.inverted().multiplied(rawTagPoseVector);
                /* Повернуть относительное положение на угол между тегом и полем */
                VectorF rotatedPosVector = fieldTagQ.applyToVector(rawTagPoseVector);
                /* Вычесть полученное положение камеры из абсолютного положения тега */
                VectorF fieldCameraPos = fieldTagPos.subtracted(rotatedPosVector);

                xSum += fieldCameraPos.get(0);
                ySum += fieldCameraPos.get(1);
            }
        }

        _odometry.X = xSum / _aprilTagProcessor.getDetections().size();
        _odometry.Y = ySum / _aprilTagProcessor.getDetections().size();
    }
}