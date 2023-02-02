package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        Dimension dimension = new Dimension(1920, 1080);

        Vector3 cameraPos = new Vector3(0, 2, -2);
        Vector3 cameraLookAt = new Vector3(0, -0.2588190451, -0.9659258263);
        Vector3 up = new Vector3(0, 0.9659258263, -0.2588190451);
        Camera camera = new Camera(dimension, 1.7, cameraPos, cameraLookAt, up);

        Renderer renderer = Renderer.getInstance();

        Sphere sphere1 = new Sphere(Color.GREEN, new Vector3(0, 1,-5), 0.5);
        renderer.addObject(sphere1);
        Sphere sphere2 = new Sphere(Color.RED, new Vector3(-3, 2,-8), 0.9);
        renderer.addObject(sphere2);
        Sphere sphere3 = new Sphere(Color.PINK, new Vector3(2, 1.5,-7), 0.6);
        renderer.addObject(sphere3);


        BufferedImage img = camera.draw();
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
//        while (true)
//        {
//            img = camera.draw();
//            ImageIcon newFrame = new ImageIcon(img);
//            label.setIcon(newFrame);
//        }
    }
}