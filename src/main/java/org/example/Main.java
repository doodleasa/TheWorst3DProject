package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.example.Keyboard;

public class Main {

    private static Robot robot;
    private static Dimension dimension;
    private static int scaleF;
    private static int savedSF;

    private static JFrame frame;

    private static boolean superRender;
    public static void main(String[] args) throws IOException {

        frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        //dimension = new Dimension(768, 480);
        //dimension = new Dimension(100, 100);
        //dimension = new Dimension(10,10);
        //dimension = new Dimension(1920, 1080);
        //dimension = new Dimension(192, 108);
        scaleF = 15;
        savedSF = scaleF;
        dimension = new Dimension(1920/scaleF, 1080/scaleF);


        Vector3 cameraPos = new Vector3(-2, 2, 0);
        Vector3 cameraLookAt = new Vector3(1, 0, 0);
        Vector3 up = new Vector3(0, 1, 0);
        Camera camera = Camera.init(dimension, 16 / 9.0, cameraPos, cameraLookAt, up, scaleF);
        superRender = false;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        Renderer renderer = Renderer.getInstance();

        Vector3 v1 = new Vector3(0, 2, -5);
        Vector3 v2 = new Vector3(0, 2, 0);
        Vector3 v3 = new Vector3(2, 1.5, -7);

        Sphere sphere1 = new Sphere(Color.YELLOW, v1, 0.5);
        renderer.addObject(sphere1);
        Sphere sphere2 = new Sphere(Color.RED, v2, 0.9);
        renderer.addObject(sphere2);
        Sphere sphere3 = new Sphere(Color.PINK, v3, 0.6);
        renderer.addObject(sphere3);
        Plane plane1 = new Plane(Color.CYAN, v1, v3, v2);
        renderer.addObject(plane1);
        Vector3 position = new Vector3(2, 0.5, 0);
        Cube cube = new Cube(Color.darkGray, position, 2.0);
        cube.initialize();
        Plane floor1 = new Plane(Color.GREEN, new Vector3(-100, 0, -100), new Vector3(100, 0, -100), new Vector3(100, 0, 100));
        Plane floor2 = new Plane(Color.GREEN, new Vector3(-100, 0, 100), new Vector3(100, 0, 100), new Vector3(-100, 0, -100));
        renderer.addObject(floor1);
        renderer.addObject(floor2);

        BufferedImage img = camera.draw();
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label);

        frame.setCursor(frame.getToolkit().createCustomCursor(
                new BufferedImage( 1, 1, BufferedImage.TYPE_INT_ARGB ),
                new Point(),
                null ) );

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        robot.mouseMove(dimension.width * scaleF / 2, dimension.height * scaleF / 2);
        while (true) {
            movementTick();
            img = camera.draw();
            ImageIcon newFrame = new ImageIcon(img);
            label.setIcon(newFrame);
        }
    }

    private static void movementTick() {
        Camera camera = Camera.getInstance();
        if (Keyboard.isKeyPressed(32))
        {
            dimension = new Dimension(scaleF * dimension.width, scaleF * dimension.height);
            scaleF = 1;
            camera.setDimension(dimension);
            camera.setSf(scaleF);
        }
        else if(savedSF != scaleF)
        {
            scaleF = savedSF;
            dimension = new Dimension(dimension.width / scaleF, dimension.height / scaleF);
            camera.setDimension(dimension);
            camera.setSf(scaleF);
        }
        else {
            Point Mpoint = MouseInfo.getPointerInfo().getLocation();
            Point Cpoint = frame.getLocation();
            Point cCenter = new Point(dimension.width * scaleF / 2, dimension.height * scaleF /2);

            Point point = new Point(Mpoint.x - Cpoint.x - cCenter.x, Mpoint.y - Cpoint.y - cCenter.y);

            int xpos = point.x;

            if (xpos != 0)
            {
                camera.rotateS(xpos / 3.0);
            }

            if (Keyboard.isKeyPressed(87))
            {
                camera.move(Vector3.scale(Camera.getInstance().lookAt, 0.1));
            }
            if (Keyboard.isKeyPressed(83))
            {
                camera.move(Vector3.scale(Camera.getInstance().lookAt, -0.1));
            }
            if (Keyboard.isKeyPressed(65))
            {
                camera.move(Vector3.scale(Vector3.crossProduct(Camera.getInstance().lookAt, Camera.getInstance().up), -0.1));
            }
            if (Keyboard.isKeyPressed(68))
            {
                camera.move(Vector3.scale(Vector3.crossProduct(Camera.getInstance().lookAt, Camera.getInstance().up), 0.1));
            }
            if (Keyboard.isKeyPressed(27))
            {
                System.exit(0);
            }
        }
        robot.mouseMove(dimension.width * scaleF / 2, dimension.height * scaleF / 2);
    }
}