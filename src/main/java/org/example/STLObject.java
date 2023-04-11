package org.example;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class STLObject implements Cobject{

    ArrayList<Plane> planes;
    @Override
    public void initialize() {
        Renderer renderer = Renderer.getInstance();
        for (Plane plane : planes) {
            renderer.addObject(plane);
        }
    }

    public STLObject(Path filepath, Color color, Vector3 offSet) throws IOException {
        planes = new ArrayList<>();
        ArrayList<Triangle> triangles;
        try {
            triangles = (ArrayList<Triangle>) STLParser.parseSTLFile(filepath);
        } catch (IOException e) {
            System.out.println("STL parsing failed!");
            throw e;
        }
        for (Triangle triangle :triangles) {
             planes.add(triangle.convertToPlane(color, offSet));
        }
    }
}
