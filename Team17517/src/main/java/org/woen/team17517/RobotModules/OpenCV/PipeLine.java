package org.woen.team17517.RobotModules.OpenCV;


import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.concurrent.atomic.AtomicReference;

//@Config
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
    Mat img_range_red1 = new Mat();
    Mat img_range_red2 = new Mat();
    Mat img_range_blue = new Mat();
    public static double hRedDown1 = 160;
    public static double hRedDown2 = 0;
    public static double cRedDown = 100;
    public static double vRedDown = 130;
    public static double hRedUp1 = 180;
    public static double hRedUp2 = 20;
    public static double cRedUp = 255;
    public static double vRedUp = 255;

    public static double hBlueDown = 90;
    public static double cBlueDown = 150;
    public static double vBlueDown = 100;
    public static double hBlueUp = 150;
    public static double cBlueUp = 255;
    public static double vBlueUp = 255;

    public double cx = 0;

    double x1Finish = x * 0.4;
    double x1Start = x * 0;
    double x2Finish = x * 0.7;
    double x2Start = x * 0.4;
    double x3Finish = x;
    double x3Start = x * 0.7;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public int pos = 0;
    public boolean team = true;

    private final AtomicReference<Bitmap> lastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));


    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        //
        cvtColor(frame, frame, COLOR_RGB2HSV);
        resize(frame, frame, new Size(x, y));

        new Mat(frame, new Rect(0, 0, (int) x, (int) y / 2)).copyTo(frame);


        inRange(frame, new Scalar(hRedDown1, cRedDown, vRedDown), new Scalar(hRedUp1, cRedUp, vRedUp), img_range_red1);
        inRange(frame, new Scalar(hBlueDown, cBlueDown, vBlueDown), new Scalar(hBlueUp, cBlueUp, vBlueUp), img_range_blue);
        inRange(frame, new Scalar(hRedDown2, cRedDown, vRedDown), new Scalar(hRedUp2, cRedUp, vRedUp), img_range_red2);

        Core.bitwise_or(img_range_red2, img_range_red1, img_range_red);
        Core.bitwise_or(img_range_red, img_range_blue, frame);


        Moments moments = Imgproc.moments(frame);

        cx = moments.m10 / moments.m00;
        double cy = moments.m01 / moments.m00;

      //  Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);
       // Utils.matToBitmap(frame, b);
       // lastFrame.set(b);


        if (cx <= 240) {
            pos = 1;
        }
        if (cx < 443 && cx >= 240) {
            pos = 2;
        }
        if (cx >= 443) {
            pos = 3;
        }

        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

  /*  @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame.get()));
    }

   */
}