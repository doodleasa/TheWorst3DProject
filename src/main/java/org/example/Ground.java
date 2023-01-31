package org.example;

import java.awt.*;

public class Ground implements Renderable{

    Color color;

    static Ground ground;
    private Ground()
    {
        color = Color.ORANGE;
    }

    public static Ground getInstance()
    {
        if (ground == null)
        {
            ground = new Ground();
        }
        return ground;
    }
    @Override
    public Double collides(Vector3 pos, Vector3 rot) {
        Double yInitial = pos.y;
        Double DY = rot.y;
        return 0 - (yInitial/DY);
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public boolean isShaded() {
        return true;
    }

    @Override
    public Double collidesOtherSide(Vector3 pos, Vector3 rot) {
        return -1.0;
    }
}
