package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Camera extends JPanel {


    Vector3 position;
    Vector3 lookAt;

    Vector3 perp;

    Vector3 up;

    int width;

    int height;

    double shiftPerPixel;



    public Camera(Dimension size, double ratio)
    {
        width = size.width;
        height = size.height;
        position = new Vector3(0, 1, 0);
        lookAt = new Vector3(0, 0, -1);
        up = new Vector3(0, 1,0);
        shiftPerPixel = ratio/(width-1);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public Camera(Dimension size, double ratio, Vector3 position, Vector3 lookAt, Vector3 up)
    {
        lookAt.normalize();
        up.normalize();

        width = size.width;
        height = size.height;

        this.position = position;
        this.lookAt = lookAt;
        this.up = up;

        shiftPerPixel = ratio/(width-1);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public BufferedImage draw()
    {
        Renderer scene = Renderer.getInstance();
        BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for(int i = 0; i < width * height; i++)
        {
            Vector3 ray = new Vector3(lookAt.x, lookAt.y, lookAt.z);
            int x = i % width;
            int y = i / width;
            double xShifts = (x) - ((double) width/2);
            double yShifts = ((double) height /2) - (y);
            Vector3 LRShift = Vector3.scale(perp, xShifts * shiftPerPixel);
            Vector3 UDShift = Vector3.scale(up, yShifts * shiftPerPixel);
            ray = Vector3.add(ray, LRShift);
            ray = Vector3.add(ray, UDShift);
            ray.normalize();
            Color color = scene.render(position, ray);
            imageOut.setRGB(x, y, color.getRGB());
        }
        return imageOut;
    }
}
