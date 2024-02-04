package org.woen.team17517.RobotModules.Transport;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;

public class TransportPixels implements RobotModule {
    UltRobot robot;
    public TransportPixels(UltRobot robot){
        this.robot = robot;
    }
    public void update(){
        switch (robot.lift.getPosition()){
            case DOWN:
                robot.grabber.open();
                robot.grabber.down();
                break;
            case UP:
                robot.grabber.finish();
                break;
            case UNKNOWN:
                robot.grabber.safe();
                robot.grabber.close();
                break;
            }
        robot.lift.update();
        robot.grabber.update();
    }

    @Override
    public boolean isAtPosition() {
        return robot.lift.isAtPosition();
    }
}
