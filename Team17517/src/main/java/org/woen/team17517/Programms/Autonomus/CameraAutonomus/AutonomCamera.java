package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.Programms.Autonomus.OldAutonomus.AutonomBaseClass;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;

@Autonomous
public class AutonomCamera extends LinearOpMode {
    UltRobot robot;
    boolean dpadUp = false;
    boolean dpadDown = false;
    boolean dpadRight = false;
    Button dpadRightButton = new Button();
    boolean dpadLeft = false;
    Button dpadLeftButton = new Button();
    Button dpadDownButton = new Button();
    Button dpadUpButton = new Button();
    boolean rightBumper = false;
    Button rightBumperButton   = new Button();
    boolean rightTriger = false;
    Button rigtTrigerButton = new Button();
    StartTeam startTeam = StartTeam.BlUE;
    StartPosition startPosition = StartPosition.RIGHT;
    PipeLine pipeLine;
    @Override
    public void waitForStart() {
        long  timeToSleep= 0;
        while (!isStarted()) {
            dpadDown = gamepad1.dpad_down;
            dpadUp = gamepad1.dpad_up;
            dpadLeft = gamepad1.dpad_left;
            dpadRight = gamepad1.dpad_right;
            rightBumper = gamepad1.right_bumper;
            rightTriger = gamepad1.right_trigger > 0;

            if (dpadDownButton.update(dpadDown)){
                startTeam = StartTeam.BlUE;
            }
            if (dpadUpButton.update(dpadUp)){
                startTeam = StartTeam.RED;
            }
            if (dpadLeftButton.update(dpadLeft)){
                startPosition = StartPosition.LEFT;
            }
            if (dpadRightButton.update(dpadRight)){
                startPosition = StartPosition.RIGHT;
            }
            if (rightBumperButton.update(rightBumper)){
                timeToSleep += 1000;
            }
            if (rigtTrigerButton.update(rightTriger)){
                timeToSleep += 5000;
            }

            telemetry.addData("Team",startTeam);
            telemetry.addData("Position",startPosition);
            telemetry.addData("Time to sleep", timeToSleep);
            telemetry.update();
        }
        sleep(timeToSleep);
    }


    enum StartTeam{
        BlUE,
        RED
    }
    enum StartPosition{
        RIGHT,
        LEFT
    }

    public void runOpMode() {
        pipeLine = new PipeLine();
        robot = new UltRobot(this);
        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();

        waitForStart();
        ElementPosition elementPos = ElementPosition.NOTFIND;
        if (pipeLine.pos == 1)
            elementPos = ElementPosition.LEFT;
        if (pipeLine.pos == 2)
            elementPos = ElementPosition.FRONT;
        if (pipeLine.pos == 3)
            elementPos = ElementPosition.RIGHT;
        switch (elementPos){
            case FRONT:
                robot.autnomModules.Move(0,24000,0,1);
                robot.autnomModules.Move(0,-25000,0,1);
                break;
            case LEFT:
                robot.autnomModules.Move(0,24000,4000,0);
                robot.autnomModules.Move(0,-24000,-4000,0);
                break;
            case RIGHT:
                robot.autnomModules.Move(0,24000,-4000,0);
                robot.autnomModules.Move(0,-24000,4000,0);
                break;
        }
        switch (startTeam){
            case RED:
        }

    }
}