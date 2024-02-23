package org.woen.team18742.Tools.Motor;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class EncoderOnly extends DcMotorImplEx {
    public EncoderOnly(DcMotorController controller, int portNumber) {
        super(controller, portNumber);
    }

    @Override
    public synchronized void setPower(double power) {
        throw new RuntimeException("attempt to switch power on encoder");
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        throw new RuntimeException("attempt to get amps on encoder");
    }
}
