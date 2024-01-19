package org.woen.team18742.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Motor.Motor;
import org.woen.team18742.Tools.Motor.MotorsHandler;
import org.woen.team18742.Tools.Motor.ReductorType;
import org.woen.team18742.Tools.Timers.Timer;
import org.woen.team18742.Tools.Timers.TimerHandler;
import org.woen.team18742.Tools.ToolTelemetry;

@TeleOp
public class MotorTestOpMode extends LinearOpMode {
    Runnable timerAction = null;

    @Override
    public void runOpMode() throws InterruptedException {
        MotorsHandler handler = new MotorsHandler();
        TimerHandler timersHandler = new TimerHandler();

        Timer timer1 = new Timer(), timer2 = new Timer(), timer3 = new Timer();

        Motor m = new Motor(hardwareMap.get(DcMotorEx.class, "leftmotor"), ReductorType.TWENTY);

        ToolTelemetry.SetTelemetry(telemetry);

        waitForStart();
        resetRuntime();

        timerAction = ()->{
            m.setPower(1);
            timer2.Start(3000, ()->{
                m.setPower(0.4);
                timer3.Start(3000, ()->{
                    m.setPower(0.1);
                    timer1.Start(3000, timerAction);
                });
            });
        };

        timer1.Start(0, timerAction);

        handler.Start();

        while (opModeIsActive()){
            handler.Update();
            ToolTelemetry.Update();
            timersHandler.Update();
        }
    }
}
