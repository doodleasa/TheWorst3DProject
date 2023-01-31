package org.example;

import java.awt.*;

public class SkyBox implements Renderable{

    Color color;

    static SkyBox skyBox;

    private SkyBox(Color color)
    {
        this.color = color;
    }

    public static SkyBox getInstance()
    {
        if (skyBox == null)
        {
            skyBox = new SkyBox(Color.BLUE);
        }
        return skyBox;
    }
    @Override
    public Double collides(Vector3 p1, Vector3 p2) {
        return Double.MAX_VALUE;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isShaded() {
        return false;
    }

    @Override
    public Double collidesOtherSide(Vector3 pos, Vector3 rot) {
        return -1.0;
    }
}
