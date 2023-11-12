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
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
@Config
public class Speed extends LinearOpMode {
    private DcMotorEx motor;
    private VoltageSensor voltageSensor;
    public static double ki = 0.0015;
    public static double kp = 0.0048;
    public static double kd = 0.002;
    double time = System.currentTimeMillis() / 1000.0;
    public static double target = 2000;
    double Lastwrong = 0;
    double I = 0;
    double D = 0;
    double C = 0;
    double F;
    double time1= 0;
    double doing = 0;
    public void runOpMode() {
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();
        Telemetry dualTelemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

        waitForStart();
        while (opModeIsActive()) {
            time1 = System.currentTimeMillis() / 1000.0-time;
            double enc = motor.getVelocity();

            double wrong = target - enc;

            double P = kp * wrong;
            I += wrong * time1 * ki;

            //F = target*F;

            D = ((wrong - Lastwrong) / time1) * kd;

            Lastwrong = wrong;

            C = 12/voltageSensor.getVoltage();
            double PID = ((I + D + P + target)/2360)*C;

            dualTelemetry.addData("Encoder",enc);
            dualTelemetry.addData("target",target);

            motor.setPower(PID);

            //motor.setPower(PID/2100);
            //dualTelemetry.addData("Doing",PID/2100);
            dualTelemetry.update();
            //doing = Range.clip(PID,-1,1);

        }
    }
}