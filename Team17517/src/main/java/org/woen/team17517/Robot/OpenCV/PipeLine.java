package org.woen.team17517.Robot.OpenCV;


import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

import android.graphics.Camera;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.woen.team17517.Robot.UltRobot;

public class PipeLine implements VisionProcessor {
    double x = 640;
    double y = 480;
    double r1 = 5;
    double g1 = 69;
    double b1 = 206;
    double r2 = 60;
    double g2 = 129;
    double b2 = 296;
    Mat img_range_red;
    Mat img_range_blue;
    UltRobot robot;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public int pos = 0;


    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        cvtColor(frame, frame, Imgproc.COLOR_RGBA2GRAY);
        resize(frame, frame, new Size(x, y));

        blur(frame, frame, new Size(10, 10));

        inRange(frame, new Scalar(0, 0, 0), new Scalar(50, 83, 221), img_range_red);
        inRange(frame, new Scalar(240, 0, 0), new Scalar(90, 82, 240), img_range_blue);

        Rect boundingRect = boundingRect(frame);

        centerOfRectX = boundingRect.x + boundingRect.width / 2;
        centerOfRectY = boundingRect.y + boundingRect.height / 2;

        if (centerOfRectX < 160 && centerOfRectX > 0) {
            pos = 1;
        }
        if (centerOfRectX < 320 && centerOfRectX > 160) {
            pos = 2;
        }
        if (centerOfRectX < 480 && centerOfRectX > 320) {
            pos = 3;
        }
        return pos;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}