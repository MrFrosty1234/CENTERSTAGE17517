package org.woen.testcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
@Config
public class MotorTest extends LinearOpMode {
    private DcMotorEx motor;
    public static double ki = 0.0000000000000015;
    public static double kp = 0.0048;
    public static double kd = 0.002;
    double time = System.currentTimeMillis() / 1000.0;
    double target = 2000;
    double Lastwrong = 0;
    double I = 0;
    double D = 0;
    double doing = 0;
    public void runOpMode() {
        //PIDCmethod h = new PIDCmethod(1,2,3,0.1);
        //PIDCmethod g = new PIDCmethod(1,2,3);
        motor = hardwareMap.get(DcMotorEx.class, "motor");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);

        Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();
        Telemetry dualTelemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

        waitForStart();
        while (opModeIsActive()) {
            time = System.currentTimeMillis() / 1000.0-time;
            int enc = motor.getCurrentPosition();

            double wrong = target - enc;

            double P = kp * wrong;


            I += wrong * time * ki;


            D = ((wrong - Lastwrong) / time) * kd;

            Lastwrong = wrong;

            double PID = I + D + P;

            dualTelemetry.addData("Encoder",enc);
            dualTelemetry.addData("target",target);
            motor.setPower(PID);
            dualTelemetry.addData("Doing",PID);
            dualTelemetry.update();


        }
    }
}