package org.example;

import java.awt.*;

public class UnresolvedPixel {
    private Vector3 ray;
    private int x;
    private int y;

    UnresolvedPixel(Vector3 ray, int x, int y)
    {
        this.ray = ray;
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

    Vector3 getRay()
    {
        return ray;
    }
}