package yellowbirb.birbstheodolite.render.buffer;

import lombok.Getter;

@Getter
public class Vertex {
    private float x;
    private float y;
    private float z;
    private float r;
    private float g;
    private float b;
    private float a;

    public Vertex(float x, float y, float z, float r, float g, float b, float a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
