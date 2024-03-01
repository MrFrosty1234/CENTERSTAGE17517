package org.woen.team18742.Tools;

import androidx.annotation.NonNull;

public class Color {
    public final static Color RED = new Color(255, 0, 0);
    public final static Color BLUE = new Color(0, 0, 255);
    public final static Color GREEN = new Color(0, 255, 0);
    public final static Color GRAY = new Color(128, 128, 128);
    public final static Color BLACK = new Color(0, 0, 0);
    public final static Color WHITE = new Color(255, 255, 255);
    public final static Color YELLOW = new Color(255, 255, 0);
    public final static Color ORANGE = new Color(255, 128, 0);

    public int R, G, B;

    public Color(int r, int g, int b){
        R = r;
        G = g;
        B = b;
    }

    @NonNull
    @Override
    public String toString() {
        if (R > 255 || G > 255 || B > 255)
            throw new RuntimeException("color more 255");

        if (R < 0 || G < 0 || B < 0)
            throw new RuntimeException("color less 0");

        String rString = Integer.toString(R, 16).toUpperCase(), gString = Integer.toString(G, 16).toUpperCase(), bString = Integer.toString(B, 16).toUpperCase();

        if(rString.length() == 1)
            rString = "0" + rString;

        if(gString.length() == 1)
            gString = "0" + gString;

        if(bString.length() == 1)
            bString = "0" + bString;

        return "#" + rString + gString + bString;
    }

    @NonNull
    @Override
    public Color clone() {
        return new Color(R, G, B);
    }
}
