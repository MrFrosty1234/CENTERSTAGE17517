package org.woen.testcode;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class AutonomF extends LinearOpMode {


    DcMotor leftperedDrive = null;
    DcMotor rightperedDrive = null;
    DcMotor leftzadDrive = null;
    DcMotor rightzadDrive = null;
    DcMotor chto = null;
    double diametr = 9.8;
    double oborot = 1440;
   double kp=1;
   double ki=1;
   double kd=1;
    double ui = 0;
    double errold;
    double told;
    double rightperedencoder = rightperedDrive.getCurrentPosition();
    double leftperedencoder = leftperedDrive.getCurrentPosition();
    double rightzadencoder = rightperedDrive.getCurrentPosition();
    double leftzadencoder = leftzadDrive.getCurrentPosition();
    double radiusrobota = 50;
    void dviz(double forward,double rotate){
        leftperedDrive.setPower(rotate + forward);
        rightperedDrive.setPower(-rotate + forward);
        leftzadDrive.setPower(rotate + forward);
        rightzadDrive.setPower(-rotate + forward);
    }

    void move(double dist) { // dist - в сантиметрах

      /*  Human romandukarevich = new Human();
        romandukarevich.age = 10.5;
        romandukarevich.name = "Roma";

        romandukarevich.hello();
*/
       Obnova rightpered = new Obnova();
       rightpered.funkcia(1);

        Obnova leftpered = new Obnova();
        leftpered.funkcia(1);

        Obnova rightback = new Obnova();
        rightback.funkcia(1);

        Obnova leftback = new Obnova();
       leftback.funkcia(1);

        double kp = 1;
        double chtotolevoep;
        double chtotopravoep;
        double chtotolevoez;
        double chtotopravoez;
        do {
            double distencoder = (dist / (diametr * PI)) * oborot;
            // диаметр колеса - 9.8 см
            // энкодер выдает 1440 на один оборот
            double rightperedencoder = rightperedDrive.getCurrentPosition();
            double leftperedencoder = leftperedDrive.getCurrentPosition();
            double rightzadencoder = rightzadDrive.getCurrentPosition();
            double leftzadencoder = leftzadDrive.getCurrentPosition();
            double errrightp = distencoder - rightperedencoder;
            double errleftp = distencoder - leftperedencoder;
            double errrightz = distencoder - rightzadencoder;
            double errleftz = distencoder - leftzadencoder;
            double uleftp = errleftp * kp;
            double urightp = errrightp * kp;
            double uleftz = errleftz * kp;
            double urightz = errrightz * kp;
            chtotolevoep = abs(errleftp);
            chtotopravoep = abs(errrightp);
            chtotolevoez = abs(errleftz);
            chtotopravoez = abs(errrightz);
            leftperedDrive.setPower(uleftp);
            rightperedDrive.setPower(urightp);
            leftzadDrive.setPower(uleftz);
            rightzadDrive.setPower(urightz);

        } while (chtotolevoep > 5 && chtotopravoep > 5 && chtotopravoez > 5 && chtotolevoez > 5 && opModeIsActive());
        leftperedDrive.setPower(0);
        rightperedDrive.setPower(0);
        leftzadDrive.setPower(0);
        rightzadDrive.setPower(0);
    }



    public double obnova(double err){
        double time = System.currentTimeMillis() / 1000.0;
        double up = err * kp;
        ui += (err * ki) * (time - told);
        if (abs(ui) > 0.25) {
            ui = 0.25;
        }
        double udr = (err - errold) * kd / (time - told);
        errold = err;
        told = time;
        return up + udr + ui;
    }



    void right(double right) {

        double kp = 1;
        double chtotolevoep;
        double chtotopravoep;
        double chtotolevoez;
        double chtotopravoez;
        do {
            double rightperedencoder = rightperedDrive.getCurrentPosition();
            double leftperedencoder = leftperedDrive.getCurrentPosition();
            double rightzadencoder = rightzadDrive.getCurrentPosition();
            double leftzadencoder = leftzadDrive.getCurrentPosition();
            double errrightp = right + rightperedencoder;
            double errleftp = right - leftperedencoder;
            double errrightz = right + rightzadencoder;
            double errleftz = right - leftzadencoder;
            double uleftp = errleftp * kp;
            double urightp = errrightp * kp;
            double uleftz = errleftz * kp;
            double urightz = errrightz * kp;
            chtotolevoep = abs(errleftp);
            chtotopravoep = abs(errrightp);
            chtotolevoez = abs(errleftz);
            chtotopravoez = abs(errrightz);
            leftperedDrive.setPower(uleftp);
            rightperedDrive.setPower(-urightp);
            leftzadDrive.setPower(uleftz);
            rightzadDrive.setPower(-urightz);

        } while (chtotolevoep > 5 && chtotopravoep > 5 && chtotopravoez > 5 && chtotolevoez > 5 && opModeIsActive());
        leftperedDrive.setPower(0);
        rightperedDrive.setPower(0);
        leftzadDrive.setPower(0);
        rightzadDrive.setPower(0);

    }
    double peremeshenie(){
        double rightencoderp = rightperedDrive.getCurrentPosition();
        double leftencoderp = leftperedDrive.getCurrentPosition();
        double rightencoderz = rightzadDrive.getCurrentPosition();
        double leftencoderz = leftzadDrive.getCurrentPosition();
        double srednee = (rightencoderp + leftencoderp + rightencoderz + leftencoderz)/4;
        double sredneedist = (srednee / (diametr * PI)) * oborot;
        return sredneedist;
    }

    double peremeshenie_v_bok(){
        double rightencoderp = rightperedDrive.getCurrentPosition();
        double leftencoderp = leftperedDrive.getCurrentPosition();
        double rightencoderz = rightzadDrive.getCurrentPosition();
        double leftencoderz = leftzadDrive.getCurrentPosition();
        double k = 0.9;
        double srednee = (rightencoderp - leftencoderp - rightencoderz + leftencoderz)/4;
        double bokom = (srednee / (diametr * PI)) * oborot * k;
        return bokom;
    }


    double uhol_v_radiani(){
        double rightencoderp = rightperedDrive.getCurrentPosition();
        double leftencoderp = leftperedDrive.getCurrentPosition();
        double rightencoderz = rightzadDrive.getCurrentPosition();
        double leftencoderz = leftzadDrive.getCurrentPosition();
        double srednee = (rightencoderp - leftencoderp + rightencoderz - leftencoderz)/4;
        double dist = (srednee/ oborot * (diametr * PI)) ;
        double radiana = dist * radiusrobota;
        return radiana;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftperedDrive = hardwareMap.get(DcMotor.class, "leftperedmotor");
        leftperedDrive.setDirection(DcMotorSimple.Direction.REVERSE); // Поставить направление мотора

        rightperedDrive = hardwareMap.get(DcMotor.class, "rightperedmotor");
        rightperedDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftzadDrive = hardwareMap.get(DcMotor.class, "leftzadmotor");
        leftzadDrive.setDirection(DcMotorSimple.Direction.REVERSE); // Поставить направление мотора

        rightzadDrive = hardwareMap.get(DcMotor.class, "rightzadmotor");
        rightzadDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        chto = hardwareMap.get(DcMotor.class, "xerznaetchto");
        chto.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
