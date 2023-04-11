package org.example;

import java.awt.*;

public class Triangle {
    private Vector3 v0;
    private Vector3 v1;
    private Vector3 v2;
    private Vector3 normal;
    Color color;

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2)
    {
        this.v0 = Vector3.swapToWorseCordinateSystem(v0);
        this.v1 = Vector3.swapToWorseCordinateSystem(v1);
        this.v2 = Vector3.swapToWorseCordinateSystem(v2);
    }

    public Plane convertToPlane(Color color, Vector3 offset, double scale)
    {
        v0 = Vector3.scale(v0, scale);
        v1 = Vector3.scale(v1, scale);
        v2 = Vector3.scale(v2, scale);


        v0 = Vector3.add(v0, offset);
        v1 = Vector3.add(v1, offset);
        v2 = Vector3.add(v2, offset);
        return new Plane(color, v0, v1, v2);
    }
}
