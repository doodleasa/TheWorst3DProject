package org.example;

import java.awt.*;
import java.util.ArrayList;

public class Cube implements Cobject{

    ArrayList<Plane> planes;

    public Cube(Color color, Vector3 position, Double size)
    {
        this(color, position, size, size, size);
    }

    public Cube(Color color, Vector3 position, Double length, Double width, double height)
    {
        planes = new ArrayList<>();

        Vector3 cv0 = position;
        Vector3 cv1 = new Vector3(position.x + length, position.y, position.z);
        Vector3 cv2 = new Vector3(position.x, position.y, position.z + width);
        Vector3 cv3 = new Vector3(position.x + length, position.y, position.z + width);
        Vector3 cv4 = new Vector3(position.x, position.y + height, position.z);
        Vector3 cv5 = new Vector3(position.x + length, position.y + height, position.z);
        Vector3 cv6 = new Vector3(position.x, position.y + height, position.z + width);
        Vector3 cv7 = new Vector3(position.x + length, position.y + height, position.z + width);

        Plane b1 = new Plane(color, cv0, cv1, cv2);
        Plane b2 = new Plane(color, cv3, cv1, cv2);
        Plane r1 = new Plane(color, cv0, cv1, cv4);
        Plane r2 = new Plane(color, cv5, cv1, cv4);
        Plane t1 = new Plane(color, cv5, cv6, cv4);
        Plane t2 = new Plane(color, cv5, cv6, cv7);
        Plane f1 = new Plane(color, cv0, cv2, cv4);
        Plane f2 = new Plane(color, cv6, cv2, cv4);
        Plane l1 = new Plane(color, cv6, cv2, cv3);
        Plane l2 = new Plane(color, cv6, cv7, cv3);
        Plane o1 = new Plane(color, cv7, cv3, cv5);
        Plane o2 = new Plane(color, cv1, cv3, cv5);

        planes.add(b1);
        planes.add(b2);
        planes.add(r1);
        planes.add(r2);
        planes.add(t1);
        planes.add(t2);
        planes.add(f1);
        planes.add(f2);
        planes.add(l1);
        planes.add(l2);
        planes.add(o1);
        planes.add(o2);
    }
    @Override
    public void initialize() {
        Renderer renderer = Renderer.getInstance();
        for(Plane plane : planes)
        {
            renderer.addObject(plane);
        }
    }
}
