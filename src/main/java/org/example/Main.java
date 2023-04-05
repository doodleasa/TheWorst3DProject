package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        Dimension dimension = new Dimension(768, 480);
        //Dimension dimension = new Dimension(1920, 1080);

        Vector3 cameraPos = new Vector3(-2, 2, 0);
        Vector3 cameraLookAt = new Vector3(1, 0, 0);
        Vector3 up = new Vector3(0, 1, 0);
        Camera camera = Camera.init(dimension, 16/9.0, cameraPos, cameraLookAt, up);

        Renderer renderer = Renderer.getInstance();

        Vector3 v1 = new Vector3(0, 2,-5);
        Vector3 v2= new Vector3(0, 2,0);
        Vector3 v3 = new Vector3(2, 1.5,-7);

        Sphere sphere1 = new Sphere(Color.GREEN, v1, 0.5);
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

        SAction sAction = new SAction();
        WAction wAction = new WAction();
        AAction aAction = new AAction();
        DAction dAction = new DAction();
        LeftAction leftAction = new LeftAction();
        RightAction rightAction = new RightAction();

        BufferedImage img = camera.draw();
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label);

        label.getInputMap().put(KeyStroke.getKeyStroke('w'), wAction);
        label.getActionMap().put(wAction, wAction);

        label.getInputMap().put(KeyStroke.getKeyStroke('s'), sAction);
        label.getActionMap().put(sAction, sAction);

        label.getInputMap().put(KeyStroke.getKeyStroke('a'), aAction);
        label.getActionMap().put(aAction, aAction);

        label.getInputMap().put(KeyStroke.getKeyStroke('d'), dAction);
        label.getActionMap().put(dAction, dAction);

        label.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), leftAction);
        label.getActionMap().put(leftAction, leftAction);

        label.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), rightAction);
        label.getActionMap().put(rightAction, rightAction);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        while (true)
        {
            img = camera.draw();
            ImageIcon newFrame = new ImageIcon(img);
            label.setIcon(newFrame);
        }
    }
    public static class WAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().move(Vector3.scale(Camera.getInstance().lookAt, 0.5));
        }
    }
    public static class SAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().move(Vector3.scale(Camera.getInstance().lookAt, -0.5));
        }
    }

    public static class AAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().move(Vector3.scale(Vector3.crossProduct(Camera.getInstance().lookAt, Camera.getInstance().up), -0.5));
        }
    }

    public static class DAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().move(Vector3.scale(Vector3.crossProduct(Camera.getInstance().lookAt, Camera.getInstance().up), 0.5));
        }
    }
    public static class RightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().rotateS(10);
        }
    }
    public static class LeftAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            Camera.getInstance().rotateS(-10);
        }
    }
}