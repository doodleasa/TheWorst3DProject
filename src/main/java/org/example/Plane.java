package org.example;

import java.awt.*;

public class Plane implements Renderable{

    private Vector3 v0;
    private Vector3 v1;
    private Vector3 v2;
    private Vector3 normal;
    Color color;

    public Plane(Color color, Vector3 v0, Vector3 v1, Vector3 v2)
    {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
        Vector3 a = Vector3.subtract(v1, v0);
        Vector3 b = Vector3.subtract(v2, v0);
        normal = Vector3.crossProduct(a,b);
        normal.normalize();
    }
    @Override
    public Double collides(Vector3 pos, Vector3 rot) {
        Double dot = Vector3.dotProduct(rot, normal);
        if (dot < 0.00001 && dot > -0.00001)
        {
            return -1.0;
        }
        Vector3 e0 = Vector3.subtract(v1, v0);
        Vector3 e1 = Vector3.subtract(v2, v0);
        Vector3 h = Vector3.crossProduct(rot, e1);
        double a = Vector3.dotProduct(e0, h);
        double f = 1.0 / a;
        Vector3 s = Vector3.subtract(pos, v0);
        double u = f * Vector3.dotProduct(s, h);
        if (u < 0.0 || u > 1.0)
        {
            return -1.0;
        }
        Vector3 q = Vector3.crossProduct(s, e0);
        double v = f * Vector3.dotProduct(rot, q);
        if (v < 0.0 || u + v > 1.0)
        {
            return -1.0;
        }
        double t = f * Vector3.dotProduct(e1, q);
        if (t > 0.00001)
        {
            return t;
        }
        else
        {
            return -1.0;
        }

    }
    @Override
    public Color getColor() {
        return color;
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
