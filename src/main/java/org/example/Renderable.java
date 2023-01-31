package org.example;

import java.awt.*;

public interface Renderable {
    public Double collides(Vector3 pos, Vector3 rot);
    public Color getColor();

    public boolean isShaded();

    public Double collidesOtherSide(Vector3 pos, Vector3 rot);

}
