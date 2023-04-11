package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    private static Robot robot;
    private static Dimension dimension;
    private static int scaleF;

    private static int uScaleF;

    private static JFrame frame;

    private static Dimension uDimension;

    private static boolean superRender;

    public static void main(String[] args) throws IOException {
        frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        //dimension = new Dimension(768, 480);
        //dimension = new Dimension(100, 100);
        //dimension = new Dimension(10,10);
        //dimension = new Dimension(1920, 1080);
        //dimension = new Dimension(192, 108);
        scaleF = 20;
        uScaleF = 1;
        dimension = new Dimension(1920/scaleF, 1080/scaleF);
        uDimension = new Dimension(1920/uScaleF, 1080/uScaleF);
        superRender = false;


        Vector3 cameraPos = new Vector3(-2, 2, 0);
        Vector3 cameraLookAt = new Vector3(1, 0, 0);
        Vector3 up = new Vector3(0, 1, 0);
        Camera camera = Camera.init(dimension, 16 / 9.0, cameraPos, cameraLookAt, up, scaleF);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        Renderer renderer = Renderer.getInstance();

        Vector3 v1 = new Vector3(0, 4, -5);
        Vector3 v2 = new Vector3(0, 2, 0);
        Vector3 v3 = new Vector3(2, 3.5, -7);

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

        Vector3 offSet = new Vector3(10, 1.5, 17);
        STLObject kleinBottle = new STLObject(Paths.get("src/main/java/org/example/objects/knight.stl"), Color.magenta, offSet, 0.1);
        kleinBottle.initialize();


        BufferedImage img = camera.draw();
        JLabel label = new JLabel(new ImageIcon(img));

        SpaceAction spaceAction = new SpaceAction();
        EscapeAction escapeAction = new EscapeAction();

        label.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), spaceAction);
        label.getActionMap().put(spaceAction, spaceAction);

        label.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), escapeAction);
        label.getActionMap().put(escapeAction, escapeAction);

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
        if(!superRender)
        {
            camera.setDimension(dimension);
            camera.setSf(scaleF);

            Vector3 movementDirection = camera.lookAt.copy();
            movementDirection.y = 0;
            movementDirection.normalize();

            Point Mpoint = MouseInfo.getPointerInfo().getLocation();
            Point Cpoint = frame.getLocation();
            Point cCenter = new Point(dimension.width * scaleF / 2, dimension.height * scaleF /2);

            Point point = new Point(Mpoint.x - Cpoint.x - cCenter.x, Mpoint.y - Cpoint.y - cCenter.y);

            int xpos = point.x;

            int ypos = point.y;

            if (xpos != 0)
            {
                camera.rotateY(xpos / 3.0);
            }
            if (ypos != 0)
            {
                double newAngle = -ypos / 3.0;
                double angleU = Vector3.angleBetweenVectors(new Vector3(0, 1, 0), camera.lookAt);
                double angleD = Vector3.angleBetweenVectors(new Vector3(0, -1, 0), camera.lookAt);
                if (angleU < newAngle)
                {
                    camera.rotateP(angleU);
                } else if (angleD < newAngle * -1)
                {
                    camera.rotateP(angleD * -1);
                } else
                {
                    camera.rotateP(newAngle);
                }
            }
            if (Keyboard.isKeyPressed(87))
            {
                camera.move(Vector3.scale(movementDirection, 0.1));
            }
            if (Keyboard.isKeyPressed(83))
            {
                camera.move(Vector3.scale(movementDirection, -0.1));
            }
            if (Keyboard.isKeyPressed(65))
            {
                camera.move(Vector3.scale(camera.perp, -0.1));
            }
            if (Keyboard.isKeyPressed(68))
            {
                camera.move(Vector3.scale(camera.perp, 0.1));
            }
        }
        else {
            camera.setDimension(uDimension);
            camera.setSf(uScaleF);
        }
        robot.mouseMove(dimension.width * scaleF / 2, dimension.height * scaleF / 2);
    }

    public static class SpaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            superRender = !superRender;
        }
    }

    public static class EscapeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }


}