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
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Vector2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//@Config
public class PipeLine implements VisionProcessor, CameraStreamSource {
    public AtomicReference<Bitmap> LastFrame = new AtomicReference<>(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));

    public AtomicReference<Vector2> RectCenter = new AtomicReference<>(new Vector2());

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

    public static boolean alyans = false;
    double centerOfRectX = 0;
    double centerOfRectY = 0;
    public AtomicInteger pos = new AtomicInteger(2);

    public boolean team = true;

    public void init(int width, int height, CameraCalibration calibration) {
        LastFrame.set(Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565));
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        cvtColor(frame, frame, COLOR_RGB2HSV);//конвертация в хсв
        resize(frame, frame, new Size(x, y));// установка разрешения

        frame = frame.submat(Configs.Camera.PruningStart, (int) y, 0, (int) x);

        blur(frame, frame, new Size(10, 10));//размытие для компенсации шумов с камеры
        // можно иф для установки цвета команды и только 1 инрейндж
        if (AutonomCollector.StartPosition == StartRobotPosition.BLUE_BACK)
            inRange(frame, new Scalar(Configs.Camera.hRedDown, Configs.Camera.cRedDown, Configs.Camera.vRedDowm), new Scalar(Configs.Camera.hRedUp, Configs.Camera.cRedUp, Configs.Camera.vRedUp), frame);

        //inRange(картинка вход, мин знач хсв, макс знач хсв, выход картинка(трешхолды))

        if (AutonomCollector.StartPosition == StartRobotPosition.RED_BACK)
            inRange(frame, new Scalar(Configs.Camera.hBlueDown, Configs.Camera.cBlueDown, Configs.Camera.vBlueDowm), new Scalar(Configs.Camera.hBlueUp, Configs.Camera.cBlueUp, Configs.Camera.vBlueUp), frame);

        //Core.bitwise_or(img_range_red, img_range_blue, frame);//объединяем два инрейнджа

        erode(frame, frame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize))); // Сжать
        dilate(frame, frame, getStructuringElement(MORPH_ERODE, new Size(Configs.Camera.ksize, Configs.Camera.ksize))); // Раздуть

        Bitmap b = Bitmap.createBitmap(frame.width(), frame.height(), Bitmap.Config.RGB_565);//выводим картинку в дашборд
        Utils.matToBitmap(frame, b);
        LastFrame.set(b);

        Rect boundingRect = boundingRect(frame);//boudingRect представляем прямоугольник

        if (boundingRect.area() <= 0) {
            if (AutonomCollector.StartPosition == StartRobotPosition.BLUE_BACK)
                pos.set(3);
            else
                pos.set(1);

            return frame;
        }

        centerOfRectX = boundingRect.x + boundingRect.width / 2.0;//координаты центра вычисляем
        centerOfRectY = boundingRect.y + boundingRect.height / 2.0;

        RectCenter.set(new Vector2(centerOfRectX, centerOfRectY));

        if (centerOfRectX < Configs.Camera.ZoneLeftEnd)
            if (AutonomCollector.StartPosition == StartRobotPosition.BLUE_BACK)
                pos.set(3);
            else
                pos.set(1);
        else if (centerOfRectX < Configs.Camera.ZoneForwardEnd)
            pos.set(2);
        else if (AutonomCollector.StartPosition == StartRobotPosition.BLUE_BACK)
            pos.set(1);
        else
            pos.set(3);

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
