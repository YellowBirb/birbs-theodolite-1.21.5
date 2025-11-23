package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;

import static java.lang.Math.*;

@UtilityClass
public class VertexHelper {
    public void singleLine(MatrixStack matrixStack, VertexConsumer vertexConsumer,
                                 float x0, float y0, float z0, int r0, int g0, int b0, int a0,
                                 float x1, float y1, float z1, int r1, int g1, int b1, int a1) {
        MatrixStack.Entry entry = matrixStack.peek();

        vertexConsumer.vertex(entry, x0, y0, z0).color(r0, g0, b0, a0).normal(entry, new Vector3f(x1-x0, y1-y0, z1-z0).normalize());
        vertexConsumer.vertex(entry, x1, y1, z1).color(r1, g1, b1, a1).normal(entry, 0, 0, 0);
    }

    public void lineStrip()
    {

    }

    public void circleXZ_Line_Strip(MatrixStack matrixStack, VertexConsumer vertexConsumer,
                                    float radius, float segmentLength, float x, float y, float z,
                                    int r, int g, int b, int a) {
        int estimate = (int) round((PI * radius) / segmentLength);
        circleXZ_Line_Strip(matrixStack, vertexConsumer,
                radius, estimate, x, y, z, r, g, b, a);
    }

    public void circleXZ_Line_Strip(MatrixStack matrixStack, VertexConsumer vertexConsumer,
                                     float radius, int segmentAmount, float x, float y, float z,
                                     int r, int g, int b, int a) {
        MatrixStack.Entry entry = matrixStack.peek();

        segmentAmount = max(8, segmentAmount);

        for (int i = 0; i <= segmentAmount; i++) {

            float x1 = circleX(i, segmentAmount, radius, x);
            float x2 = circleX(i + 1, segmentAmount, radius, x);
            float z1 = circleZ(i, segmentAmount, radius, z);
            float z2 = circleZ(i + 1, segmentAmount, radius, z);

            Vector3f normalVec = new Vector3f(x2-x1, 0, z2-z1).normalize();

            vertexConsumer.vertex(entry, x1, y, z1)
                    .color(r, g, b, a)
                    .normal(entry, normalVec);
        }
    }

    private float circleX(int i, int segmentAmount, float radius, float x) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float xOffset = (float) (cos(angle) * radius);
        return x + xOffset;
    }

    private float circleZ(int i, int segmentAmount, float radius, float z) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float zOffset = (float) (sin(angle) * radius);
        return z + zOffset;
    }
}
