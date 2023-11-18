package org.woen.testcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
@Config
public class SpeedTest extends LinearOpMode {
    private DcMotorEx motor;
    public static double power=0;
    public void runOpMode(){
        waitForStart();
        while (opModeIsActive()) {
            motor = hardwareMap.get(DcMotorEx.class, "motor");
            motor.setDirection(DcMotorSimple.Direction.FORWARD);

            Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();
            Telemetry dualTelemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

            motor.setPower(power);
            double speed = motor.getVelocity();
            dualTelemetry.addData("speed", speed);
            dualTelemetry.update();
        }
    }
}