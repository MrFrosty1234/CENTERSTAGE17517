package org.woen.team18742.Modules.Camera;

import static org.opencv.core.Core.*;
import static org.opencv.imgproc.Imgproc.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//@Config
public class PipeLine implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

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
    public static double hRedDown = 4;
    public static double cRedDown = 127.7;
    public static double vRedDowm = 154.4;
    public static double hRedUp = 30;
    public static double cRedUp = 255;
    public static double vRedUp = 255;

    public static double hBlueDown = 85;
    public static double cBlueDown = 53.8;
    public static double vBlueDowm = 148.8;
    public static double hBlueUp = 98.5;
    public static double cBlueUp = 255;
    public static double vBlueUp = 255;
    public static boolean alyans = false;
    double x2Finish = x;
    double x2Start = x * 0.6;
    double x3Finish = 0.6 * x;
    double x3Start = 0;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public AtomicInteger pos = new AtomicInteger(2);

    public int ksize = 13;
    public boolean team = true;

    public void init(int width, int height, CameraCalibration calibration) {
        LastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        //

        cvtColor(frame, frame, COLOR_RGB2HSV);//конвертация в хсв
        resize(frame, frame, new Size(x, y));// установка разрешения

        blur(frame, frame, new Size(10, 10));//размытие для компенсации шумов с камеры
        // можно иф для установки цвета команды и только 1 инрейндж
        inRange(frame, new Scalar(hRedDown, cRedDown, vRedDowm), new Scalar(hRedUp, cRedUp, vRedUp), img_range_red);

        //inRange(картинка вход, мин знач хсв, макс знач хсв, выход картинка(трешхолды))
        inRange(frame, new Scalar(hBlueDown, cBlueDown, vBlueDowm), new Scalar(hBlueUp, cBlueUp, vBlueUp), img_range_blue);



        Core.bitwise_or(img_range_red, img_range_blue, frame);//объединяем два инрейнджа

        erode(frame, frame, getStructuringElement(MORPH_ERODE, new Size(ksize, ksize))); // Сжать
        dilate(frame, frame, getStructuringElement(MORPH_ERODE, new Size(ksize, ksize))); // Раздуть

        Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(frame, b);
        LastFrame.set(b);

        Rect boundingRect = boundingRect(frame);//boudingRect рисуем прямоугольник

        if(boundingRect == null || boundingRect.area() <= 0)
            return frame;

        centerOfRectX = boundingRect.x + boundingRect.width / 2.0;//координаты центра вычисляем
        centerOfRectY = boundingRect.y + boundingRect.height / 2.0;

        if (centerOfRectX < x2Finish && centerOfRectX > x2Start) {
            pos.set(2);
        }
        else if (centerOfRectX < x3Finish && centerOfRectX > x3Start) {
            pos.set(3);
        }
        else
            pos.set(1);

        return frame;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(LastFrame.get()));
    }
}
