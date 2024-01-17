package org.woen.team18742.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team18742.Tools.Motor.Motor;
import org.woen.team18742.Tools.Motor.MotorsHandler;
import org.woen.team18742.Tools.Motor.ReductorType;

@TeleOp
public class MotorTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MotorsHandler handler = new MotorsHandler();

        Motor m = new Motor(hardwareMap.get(DcMotorEx.class, "leftmotor"), ReductorType.SIXTY);

        waitForStart();
        resetRuntime();

        handler.Start();

        while (opModeIsActive()){
            handler.Update();
        }
    }
}
