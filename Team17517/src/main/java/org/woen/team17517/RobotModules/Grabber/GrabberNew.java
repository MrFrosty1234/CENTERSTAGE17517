package org.woen.team17517.RobotModules.Grabber;

import org.woen.team17517.Service.RobotModule;

public class GrabberNew implements RobotModule {
    private GrabberPosition target = GrabberPosition.DOWN;
    private GrabberMode grabberMode = GrabberMode.FULLPROTECTION;
    public void update(){
        switch(grabberMode){
            case FULLPROTECTION:
                switch (target){
                    
                }
        }
    }
}
