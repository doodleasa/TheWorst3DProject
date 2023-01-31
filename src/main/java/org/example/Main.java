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
        Camera camera = new Camera(dimension, 1);
        Renderer renderer = Renderer.getInstance();
        Sphere sphere = new Sphere(Color.GREEN, new Vector3(0, 2,-5), 0.5);
        renderer.addObject(sphere);
        BufferedImage img = camera.draw();
        JLabel label = new JLabel(new ImageIcon(img));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
        
        while (true)
        {
            img = camera.draw();
            ImageIcon newFrame = new ImageIcon(img);
            label.setIcon(newFrame);
        }
    }
}