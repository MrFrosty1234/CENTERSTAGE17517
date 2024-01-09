package org.woen.team17517.Service;

public interface RobotModule {
    default boolean isAtPosition(){
        return true;
    }
    void update();
}
