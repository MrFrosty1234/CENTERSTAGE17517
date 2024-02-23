package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

public class MotorOnly extends DcMotorImplEx {
    public MotorOnly(DcMotorController controller, int portNumber) {
        super(controller, portNumber);
    }

    @Override
    public synchronized int getCurrentPosition() {
        throw new RuntimeException("attempt to get encoder position on only motor");
    }

    @Override
    public synchronized double getVelocity() {
        throw new RuntimeException("attempt to get encoder velocity on only motor");
    }
}
