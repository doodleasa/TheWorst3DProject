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

    public static Vector3 rotateY(Vector3 v, double angle)
    {
        Vector3 y = new Vector3(0, 1, 0);
        return rotateA(v, y, angle);
    }

    public static Vector3 rotateA(Vector3 v, Vector3 k, double angle)
    {
        k.normalize();
        v.normalize();
        double angleR = Math.toRadians(angle);
        double sin = Math.sin(angleR);
        double cos = Math.cos(angleR);

        Vector3 r = Vector3.add(Vector3.add(Vector3.scale(v, cos), Vector3.scale(Vector3.crossProduct(k, v), sin)), Vector3.scale(Vector3.scale(k, Vector3.dotProduct(k, v)), 1 - cos));
        return r;
    }

    public static Vector3 inverse(Vector3 v)
    {
        System.out.println(v);
        Vector3 newV =  Vector3.subtract(new Vector3(0.0, 0.0 , 0.0), v);
        System.out.println(newV);
        return newV;
    }

    @Override
    public String toString()
    {
        return "X: " + x + " y: " + y + " z: " + z;
    }

    public Vector3 copy()
    {
        return new Vector3(x, y, z);
    }

    public static double angleBetweenVectors(Vector3 v1, Vector3 v2)
    {
        double dot = Vector3.dotProduct(v1, v2);
        double divisor = v1.getLength() * v2.getLength();
        return Math.toDegrees(Math.acos(dot/divisor));
    }

    public static Vector3 swapToWorseCordinateSystem(Vector3 v)
    {
        double temp = v.y;
        v.y = v.z;
        v.z = temp;
        return v;
    }
}
