package org.woen.team18742.Tools;

import android.util.Pair;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.woen.team18742.Modules.Manager.BulkInit;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.Configs.DataManager;

public class Bios {
    private static Gamepad _gamepad;
    private static final Pair<String, StartRobotPosition>[] _startRobotPositionVariants = new Pair[]{
            new Pair<>("RedBack", StartRobotPosition.RED_BACK),
            new Pair<>("RedForward", StartRobotPosition.RED_FORWARD),
            new Pair<>("BlueBack", StartRobotPosition.BLUE_BACK),
            new Pair<>("BlueForward", StartRobotPosition.BLUE_FORWAD),
    };

    private static int _selectedVariant = 2;
    private static final String _key = "startPos";

    public Bios(Gamepad gamepad){
        _gamepad = gamepad;
    }

    @BulkInit
    public static void BulkInit(){
        _selectedVariant = DataManager.ReadInt(_key, 0);
    }

    private boolean _oldDown = false, _oldUp = false;

    public void Update(){
        boolean up = _gamepad.dpad_up;
        boolean down = _gamepad.dpad_down;

        if(!_oldDown && down)
            _selectedVariant = (_selectedVariant + 1) % _startRobotPositionVariants.length;

        if(!_oldUp && up){
            _selectedVariant--;

            if(_selectedVariant < 0)
                _selectedVariant = _startRobotPositionVariants.length - 1;
        }

        ToolTelemetry.AddLine("startPosition = " + _startRobotPositionVariants[_selectedVariant].first);

        _oldDown = down;
        _oldUp = up;
    }

    public void Stop(){
        DataManager.AddInt(_key, _selectedVariant);
    }

    public static StartRobotPosition GetStartPosition(){
        return _startRobotPositionVariants[_selectedVariant].second;
    }
}
