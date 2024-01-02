package org.woen.team18742.Tools.Configs;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.SharedPreferences;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.R;

public class DataManager {
    private static SharedPreferences _data;

    public static void AddDouble(String key, double data){
       AddLong(key, Double.doubleToRawLongBits(data));
    }

    public static void AddInt(String key, int data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putInt(key, data).apply();
    }

    public static void AddLong(String key, long data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putLong(key, data).apply();
    }

    public static void AddBoolean(String key, boolean data){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        _data.edit().putBoolean(key, data).apply();
    }

    public static double ReadDouble(String key, double defaultVal){
        return Double.longBitsToDouble(ReadLong(key, Double.doubleToRawLongBits(defaultVal)));
    }

    public static int ReadInt(String key, int defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getInt(key, defaultVal);
    }

    public static long ReadLong(String key, long defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getLong(key, defaultVal);
    }

    public static boolean ReadBoolean(String key, boolean defaultVal){
        if(_data == null)
            _data = AppUtil.getInstance().getActivity().getPreferences(Context.MODE_PRIVATE);

        return _data.getBoolean(key, defaultVal);
    }
}
