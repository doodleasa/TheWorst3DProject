package org.example;

import java.awt.*;

public class Pixel {
    int x;
    int y;
    Color color;

    Pixel(Color color, int x, int y)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    int getX()
    {
        return x;
    }

    int getY()
    {
        return y;
    }

    Color getColor()
    {
        return color;
    }

    int getRGB()
    {
        return color.getRGB();
    }
}
