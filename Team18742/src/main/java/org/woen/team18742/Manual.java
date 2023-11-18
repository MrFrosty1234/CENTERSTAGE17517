package org.woen.team18742;

public class Manual {
    private BaseCollector _collector;

    public Manual(BaseCollector collector) {
        _collector = collector;
    }

    public void Update() {
        _collector.Driver.DriveDirection(
                _collector.CommandCode.gamepad1.left_stick_y,
                _collector.CommandCode.gamepad1.left_stick_x,
                _collector.CommandCode.gamepad1.right_stick_x);
    }
}
