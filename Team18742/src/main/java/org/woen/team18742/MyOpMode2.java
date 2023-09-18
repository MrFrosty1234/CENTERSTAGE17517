// В начале файла обозначается пакет (папка), в котором он находится
package org.woen.team18742;

// Затем идет импорт внешних классов или функций или из них

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/*
 * Пометка или аннотация над объявлением класса добавляет его
 * в меню DS как TeleOp или Autonomous соответственно
 */
@TeleOp
/*
 * Название класса должно соответствовать названию файла
 * "extends LinearOpMode" делает класс (файл) запускаемой программой
 */
public class MyOpMode2 extends LinearOpMode {

    DcMotor myMotor = null; // Объявляем мотор

    @Override
    public void runOpMode() {
        // Привязываем к конфигурации
        myMotor = hardwareMap.dcMotor.get("My Motor");
        // Меняем направление мотора
        myMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // Остановить и сбросить энкодер
        myMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Использвать прямой режим управления
        // питанием, без коррекции скорости по энкодеру
        myMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        // Подать +100% мощности на мотор
        myMotor.setPower(1.0);
        sleep(1000);
        // Записать значение энкодера в переменную
        int enc = myMotor.getCurrentPosition();
        // Остановить мотор
        myMotor.setPower(0.0);

        // ...

    }
}
