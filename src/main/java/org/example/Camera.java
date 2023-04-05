package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

public class Camera extends JPanel {


    Vector3 position;
    Vector3 lookAt;

    Vector3 perp;

    Vector3 up;

    int width;

    int height;

    double shiftPerPixel;

    Renderer renderer;

    BufferedImage imageOut;

    private static Camera instance;



    public Camera(Dimension size, double ratio)
    {
        width = size.width;
        height = size.height;
        position = new Vector3(0, 1, 0);
        lookAt = new Vector3(0, 0, -1);
        up = new Vector3(0, 1,0);
        shiftPerPixel = ratio/(width-1);
        perp = Vector3.crossProduct(lookAt, up);
        renderer = Renderer.getInstance();
    }

    private Camera(Dimension size, double ratio, Vector3 position, Vector3 lookAt, Vector3 up)
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

    public static Camera init(Dimension size, double ratio, Vector3 position, Vector3 lookAt, Vector3 up)
    {
        instance = new Camera(size, ratio, position, lookAt, up);
        return instance;
    }

    public static Camera getInstance()
    {
        return instance;
    }

    public BufferedImage draw()
    {
        renderer = Renderer.getInstance();
        imageOut = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        int size = width * height;
        Stream.iterate(0, n -> n + 1)
                .limit(width * height)
                .map(this::ray)
                .map(pixel -> new Pixel(renderer.render(position, pixel.getRay()), pixel.getX(), pixel.getY()))
                .forEach(pixel -> imageOut.setRGB(pixel.getX(), pixel.getY(), pixel.getRGB()));
        return imageOut;
    }

    private UnresolvedPixel ray (int index)
    {
        Vector3 ray = new Vector3(lookAt.x, lookAt.y, lookAt.z);
        int x = index % width;
        int y = index / width;
        double xShifts = (x) - ((double) width/2);
        double yShifts = ((double) height /2) - (y);
        Vector3 LRShift = Vector3.scale(perp, xShifts * shiftPerPixel);
        Vector3 UDShift = Vector3.scale(up, yShifts * shiftPerPixel);
        ray = Vector3.add(ray, LRShift);
        ray = Vector3.add(ray, UDShift);
        ray.normalize();
        return new UnresolvedPixel(ray, x, y);
    }
    public void move(Vector3 shift)
    {
        position = Vector3.add(shift, position);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public void rotateS(double angle)
    {
        lookAt = Vector3.rotateXZ(lookAt, angle);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public void rotateU(double angle)
    {
        lookAt = Vector3.rotateXZ(lookAt, angle);
        perp = Vector3.crossProduct(lookAt, up);
    }

}