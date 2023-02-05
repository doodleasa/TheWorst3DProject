package org.example;

import java.awt.*;

public class Sphere implements Renderable{

    Color color;
    Vector3 position;
    double radius;

    public Sphere(Color color, Vector3 position, double radius)
    {
        this.color = color;
        this.position = position;
        this.radius = radius;
    }

    @Override
    public Double collides(Vector3 pos, Vector3 rot) {
        Vector3 rayToCenter = Vector3.subtract(position, pos);
        double distanceToClosestPoint = Vector3.dotProduct(rayToCenter, rot);
        if (distanceToClosestPoint < 0)
        {
            return -1.0;
        }
        double distanceFromClosestPointToCenter = Math.sqrt(Math.pow(rayToCenter.getLength(), 2) - Math.pow(distanceToClosestPoint, 2));
        double distanceFromClosestPointToIntersection = Math.sqrt(Math.pow(radius, 2) - Math.pow(distanceFromClosestPointToCenter, 2));
        double distanceFromRayOriginToIntersection = distanceToClosestPoint - distanceFromClosestPointToIntersection;
        return distanceFromRayOriginToIntersection;
    }

    @Override
    public Double collidesOtherSide(Vector3 pos, Vector3 rot) {
        Vector3 rayToCenter = Vector3.subtract(position, pos);
        double distanceToClosestPoint = Vector3.dotProduct(rayToCenter, rot);
        if (distanceToClosestPoint < 0)
        {
            return -1.0;
        }
        double distanceFromClosestPointToCenter = Math.sqrt(Math.pow(rayToCenter.getLength(), 2) - Math.pow(distanceToClosestPoint, 2));
        double distanceFromClosestPointToIntersection = Math.sqrt(Math.pow(radius, 2) - Math.pow(distanceFromClosestPointToCenter, 2));
        double distanceFromRayOriginToIntersection = distanceToClosestPoint + distanceFromClosestPointToIntersection;
        return distanceFromRayOriginToIntersection;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public boolean isShaded() {
        return true;
    }


    public void setPosition(Vector3 position)
    {
        this.position = position;
    }

    public void setY(double Y)
    {
        position.y = Y;
    }

}
