/* Copyright (c) 2023 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.woen.testcode.apriltag;

import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.util.Size;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.internal.camera.names.BuiltinCameraNameImpl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortalImpl;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.List;

/*
 * This OpMode illustrates the basics of AprilTag recognition and pose estimation,
 * including Java Builder structures for specifying Vision parameters.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp
public class AprilTagTest extends LinearOpMode {

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    private CameraName cameraName;
    private OpenCvCamera cvCamera;

    private FtcDashboard dashboard;

    @Override
    public void runOpMode() {
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        initAprilTag();

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();
        dashboard.startCameraStream(cvCamera, cvCamera.getCurrentPipelineMaxFps());
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                telemetryAprilTag();

                // Push telemetry to the Driver Station.
                telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                if (gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        dashboard.stopCameraStream();
        visionPortal.close();

    }   // end method runOpMode()

    public static AprilTagLibrary getCenterStageTagLibrary() {
        return new AprilTagLibrary.Builder()
                .addTag(1, "BlueAllianceLeft",
                        2, new VectorF(60.25f, 41.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(2, "BlueAllianceCenter",
                        2, new VectorF(60.25f, 35.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(3, "BlueAllianceRight",
                        2, new VectorF(60.25f, 29.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(4, "RedAllianceLeft",
                        2, new VectorF(60.25f, -29.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(5, "RedAllianceCenter",
                        2, new VectorF(60.25f, -35.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(6, "RedAllianceRight",
                        2, new VectorF(60.25f, -41.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.683f, -0.183f, 0.183f, 0.683f, 0))
                .addTag(7, "RedAudienceWallLarge",
                        5, new VectorF(-70.25f, -40.625f, 5.5f), DistanceUnit.INCH,
                        new Quaternion(0.7071f, 0, 0, -0.7071f, 0))
                .addTag(8, "RedAudienceWallSmall",
                        2, new VectorF(-70.25f, -35.125f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.7071f, 0, 0, -0.7071f, 0))
                .addTag(9, "BlueAudienceWallSmall",
                        2, new VectorF(-70.25f, 35.125f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.7071f, 0, 0, -0.7071f, 0))
                .addTag(10, "BlueAudienceWallLarge",
                        5, new VectorF(-70.25f, 40.625f, 5.5f), DistanceUnit.INCH,
                        new Quaternion(0.7071f, 0, 0, -0.7071f, 0))
                .build();
    }

    public static AprilTagLibrary getCenterStageTagLibrary2() {
        return new AprilTagLibrary.Builder()
                .addTag(1, "BlueAllianceLeft",
                        2, new VectorF(60.25f, 41.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
                .addTag(2, "BlueAllianceCenter",
                        2, new VectorF(60.25f, 35.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
                .addTag(3, "BlueAllianceRight",
                        2, new VectorF(60.25f, 29.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
                .addTag(4, "RedAllianceLeft",
                        2, new VectorF(60.25f, -29.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
                .addTag(5, "RedAllianceCenter",
                        2, new VectorF(60.25f, -35.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.3536f, -0.6124f, 0.6124f, -0.3536f, 0))
                .addTag(6, "RedAllianceRight",
                        2, new VectorF(60.25f, -41.41f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
                .addTag(7, "RedAudienceWallLarge",
                        5, new VectorF(-70.25f, -40.625f, 5.5f), DistanceUnit.INCH,
                        new Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
                .addTag(8, "RedAudienceWallSmall",
                        2, new VectorF(-70.25f, -35.125f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
                .addTag(9, "BlueAudienceWallSmall",
                        2, new VectorF(-70.25f, 35.125f, 4f), DistanceUnit.INCH,
                        new Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
                .addTag(10, "BlueAudienceWallLarge",
                        5, new VectorF(-70.25f, 40.625f, 5.5f), DistanceUnit.INCH,
                        new Quaternion(0.5f, -0.5f, -0.5f, 0.5f, 0))
                .build();
    }

    public static AprilTagLibrary getCenterStageTagLibrary3() {
        return new AprilTagLibrary.Builder()
                .addTag(10, "BlueAudienceWallLarge",
                        5, new VectorF(0, 0, 0), DistanceUnit.INCH,
                        new Quaternion(1f, 0f, 0f, 0f, 0))
                .build();
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(false)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setTagLibrary(getCenterStageTagLibrary2())
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        builder.setCamera(cameraName);
        cvCamera = OpenCvCameraFactory.getInstance().createWebcam((WebcamName) cameraName);
        //builder.setCamera(BuiltinCameraDirection.BACK);

        builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    /**
     * Add telemetry about AprilTag detections.
     */
    @SuppressLint("DefaultLocale")
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {

                AprilTagPoseRaw rawPose = detection.rawPose;
                OpenGLMatrix translationMatrix = new OpenGLMatrix();
                translationMatrix.translate((float) rawPose.x, (float) rawPose.y, (float) rawPose.z);
                translationMatrix.multiply(new OpenGLMatrix(rawPose.R));
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (ftc)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (raw)", rawPose.x, rawPose.y, rawPose.z));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (mat)", translationMatrix.get(0, 3), translationMatrix.get(1, 3), translationMatrix.get(2, 3)));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (ftc)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                Orientation rawOrientation = Orientation.getOrientation(rawPose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (raw)", rawOrientation.secondAngle, rawOrientation.thirdAngle, -rawOrientation.firstAngle));
                Orientation matOrientation = Orientation.getOrientation(translationMatrix, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (mat)", matOrientation.secondAngle, matOrientation.thirdAngle, -matOrientation.firstAngle));
                VectorF fieldPosition = detection.metadata.fieldPosition.multiplied((float) DistanceUnit.mmPerInch * .1f);
                OpenGLMatrix tagMatrix = new OpenGLMatrix();
                tagMatrix.translate(fieldPosition.get(0), fieldPosition.get(1), fieldPosition.get(2));
                tagMatrix.multiply(new OpenGLMatrix(detection.metadata.fieldOrientation.toMatrix()));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (tag)", tagMatrix.get(0, 3), tagMatrix.get(1, 3), tagMatrix.get(2, 3)));
                Orientation tagOrientation = Orientation.getOrientation(tagMatrix, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (tag)", tagOrientation.secondAngle, tagOrientation.thirdAngle, -tagOrientation.firstAngle));
                OpenGLMatrix fieldMatrix = tagMatrix.multiplied(translationMatrix);
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (fld)", fieldMatrix.get(0, 3), fieldMatrix.get(1, 3), fieldMatrix.get(2, 3)));
                Orientation fieldOrientation = Orientation.getOrientation(fieldMatrix, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (fld)", fieldOrientation.secondAngle, fieldOrientation.thirdAngle, -fieldOrientation.firstAngle));


            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

}   // end class
