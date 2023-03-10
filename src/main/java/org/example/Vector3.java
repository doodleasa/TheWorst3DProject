package org.example;

public class Vector3 {
    double x;
    double y;
    double z;

    public Vector3(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getLength()
    {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public void normalize()
    {
        double scaleF = 1/getLength();
        x *= scaleF;
        y *= scaleF;
        z *= scaleF;
    }


    public static Vector3 add(Vector3 v1, Vector3 v2)
    {
        Vector3 result = new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
        return result;
    }

    public static Vector3 subtract(Vector3 v1, Vector3 v2)
    {
        Vector3 result = new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        return result;
    }

    public static double dotProduct(Vector3 v1, Vector3 v2)
    {
        return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
    }

    public static Vector3 crossProduct(Vector3 v1, Vector3 v2)
    {
        Vector3 result = new Vector3(0, 0, 0);
        result.x = v1.y * v2.z - v1.z * v2.y;
        result.y = v1.z * v2.x - v1.x * v2.z;
        result.z = v1.x * v2.y - v1.y * v2.x;
        return result;
    }
    public static Vector3 scale(Vector3 v, double scale)
    {
        Vector3 result = new Vector3(v.x, v.y, v.z);
        result.x = result.x*scale;
        result.y = result.y*scale;
        result.z = result.z*scale;
        return result;
    }
}
