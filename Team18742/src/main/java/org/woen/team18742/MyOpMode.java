// В начале файла обозначается пакет (папка), в котором он находится
package org.woen.team18742;

// Затем идет импорт внешних классов или функций или из них
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/*
 * Пометка или аннотация над объявлением класса добавляет его
 * в меню DS как TeleOp или Autonomous соответственно
 */
@TeleOp
/*
 * Название класса должно соответствовать названию файла
 * "extends LinearOpMode" делает класс (файл) запускаемой программой
 */
public class MyOpMode extends LinearOpMode {

    /*
     * Здесь можно объявлять свои переменные и функции
     */

    /**
     * runOpMode() - главная функция, с которой начинается
     * исполнение программы
     */
    @Override
    public void runOpMode() {
        // Инициализация робота

        DcMotor myMotor = null; // Объявляем переменную-объект мотора

        // ...

        myMotor = hardwareMap.dcMotor.get("my Motor");

        /* waitForStart() - функция, ожидающая нажатие
         * кнопки "Старт" на DS после инициализации.
         */
        waitForStart();

        // То, что исполняется один раз после старта

        /*
         * Цикл в программе
         * opModeIsActive() возвращает true, пока не будет
         * нажата кнопка "Стоп" в DS. условие while(true)
         * неприемлемо, поскольку это создаст вечный цикл
         * и программа не сможет завершиться.
         */
        while (opModeIsActive()) {
            // То, что исполняется постоянно
        }
    }
}
