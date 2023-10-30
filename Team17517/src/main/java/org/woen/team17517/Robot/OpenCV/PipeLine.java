package org.woen.team17517.Robot.OpenCV;


import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class PipeLine implements VisionProcessor {
    double x = 640;
    double y = 480;
    double r1 = 5;
    double g1 = 69;
    double b1 = 206;
    double r2 = 60;
    double g2 = 129;
    double b2 = 296;
    Mat img_range_red = new Mat();
    Mat img_range_blue = new Mat();

    double x1Finish = x * 0.3;
    double x1Start = x * 0;
    double x2Finish = x * 0.6;
    double x2Start = x * 0.3;
    double x3Finish = x * 0.9;
    double x3Start = x * 0.6;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public int pos = 0;
    public boolean team = true;

    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        //
        cvtColor(frame, frame, COLOR_RGB2HSV);
        resize(frame, frame, new Size(x, y));

        blur(frame, frame, new Size(10, 10));

        inRange(frame, new Scalar(0, 0, 0), new Scalar(221, 50, 83), img_range_red);
        inRange(frame, new Scalar(240, 82, 90), new Scalar(0, 0, 240), img_range_blue);

        if (mean(frame).val[0] > 10) {
            team = true;
        } else {
            team = false;
        }


        Rect boundingRect = boundingRect(frame);

        centerOfRectX = boundingRect.x + boundingRect.width / 2;
        centerOfRectY = boundingRect.y + boundingRect.height / 2;

        if (centerOfRectX < x1Finish && centerOfRectX > x1Start) {
            pos = 1;
        }
        if (centerOfRectX < x2Finish && centerOfRectX > x2Start) {
            pos = 2;
        }
        if (centerOfRectX < x3Finish && centerOfRectX > x3Start) {
            pos = 3;
        }
        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }
}