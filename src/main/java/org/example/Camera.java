package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

    int scaleF;

    double ratio;

    private Camera(Dimension size, double ratio, Vector3 position, Vector3 lookAt, Vector3 up, int scaleF)
    {
        lookAt.normalize();
        up.normalize();

        width = size.width;
        height = size.height;

        this.scaleF = scaleF;
        this.position = position;
        this.lookAt = lookAt;
        this.up = up;
        this.ratio = ratio;

        shiftPerPixel = ratio/(width-1);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public static Camera init(Dimension size, double ratio, Vector3 position, Vector3 lookAt, Vector3 up, int scaleF)
    {
        instance = new Camera(size, ratio, position, lookAt, up, scaleF);
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
        Vector3 currentPos = position.copy();
        Vector3 currentRot = lookAt.copy();
        Vector3 currentUp = up.copy();
        Vector3 currentPerp = perp.copy();

        Stream.iterate(0, n -> n + 1)
                .limit(width * height)
                .map(index -> ray(index, currentRot, currentUp, currentPerp))
                .map(pixel -> new Pixel(renderer.render(currentPos, pixel.getRay()), pixel.getX(), pixel.getY()))
                .forEach(pixel -> imageOut.setRGB(pixel.getX(), pixel.getY(), pixel.getRGB()));
        return scale(imageOut, BufferedImage.TYPE_4BYTE_ABGR, width * scaleF, height * scaleF, scaleF, scaleF);
    }

    private UnresolvedPixel ray (int index, Vector3 lookAt, Vector3 up, Vector3 perp)
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

    public void rotateY(double angle)
    {
        angle *= -1;
        lookAt = Vector3.rotateY(lookAt, angle);
        up = Vector3.rotateY(up, angle);
        perp = Vector3.crossProduct(lookAt, up);
    }

    public void rotateP(double angle)
    {
        lookAt = Vector3.rotateA(lookAt, perp, angle);
        up = Vector3.rotateA(up, perp, angle);
        perp = Vector3.crossProduct(lookAt, up);
        lookAt.normalize();
        up.normalize();
        perp.normalize();
    }

    public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if(sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    void setDimension(Dimension dimension)
    {
        width = dimension.width;
        height = dimension.height;
        shiftPerPixel = ratio/(width-1);
    }

    void setSf(int scale)
    {
        scaleF = scale;
    }

    int getScaleF()
    {
        return  scaleF;
    }


}