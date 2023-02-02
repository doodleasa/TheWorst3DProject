package org.example;

import java.awt.*;
import java.util.ArrayList;

public class Renderer {
    ArrayList<Renderable> renderables;
    private static Renderer renderer;

    private static Vector3 sun;

    private Renderer(Vector3 sun)
    {
        renderables = new ArrayList<>();
        renderables.add(Ground.getInstance());
        Renderer.sun = sun;
    }

    public static Renderer getInstance()
    {
        if (renderer == null)
        {
            renderer = new Renderer(new Vector3(-3, 20, 5));
        }
        return renderer;
    }

    public void addObject(Renderable renderable)
    {
        renderables.add(renderable);
    }

    public Color render(Vector3 pos, Vector3 rot)
    {
        Double distance = Double.MAX_VALUE;
        Renderable rendered = SkyBox.getInstance();
        Color color = rendered.getColor();
        for(Renderable renderable: renderables)
        {
            Double distanceToCurrent = renderable.collides(pos, rot);
            if (distanceToCurrent >= 0 && distanceToCurrent <= distance)
            {
                distance = distanceToCurrent;
                color = renderable.getColor();
                rendered = renderable;
            }
        }
        if (rendered.isShaded())
        {
            Vector3 collision = Vector3.add(pos, Vector3.scale(rot, distance));
            Vector3 sunDirection = Vector3.subtract(sun, collision);
            double sunDistance = sunDirection.getLength();
            sunDirection.normalize();
            for(Renderable renderable: renderables) {
                Double distanceToCurrent;
                if (renderable == rendered)
                {
                    distanceToCurrent = renderable.collidesOtherSide(collision, sunDirection);
                }
                else {
                    distanceToCurrent = renderable.collides(collision, sunDirection);
                }
                if (distanceToCurrent >= 0 && distanceToCurrent <= sunDistance) {
                    int blue = color.getBlue() / 3;
                    int red = color.getRed() / 3;
                    int green = color.getGreen() / 3;
                    color = new Color(red, green, blue);
                }
            }
        }
        return color;
    }
}
