package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import yellowbirb.birbstheodolite.render.buffer.WorldBuffer;
import yellowbirb.birbstheodolite.render.shader.ShaderManager;

import java.awt.*;

import static org.lwjgl.opengl.GL33.*;

@UtilityClass
public class RenderUtil {

    public WorldBuffer startLines(WorldRenderContext ctx) {
        return new WorldBuffer(GL_LINES, ShaderManager.getPositionColorShader(), ctx);
    }

    public void endLines(WorldBuffer buffer) {
        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(false);
        buffer.draw();
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public void drawLine(WorldBuffer buffer, float x1, float y1, float z1, float x2, float y2, float z2, Color color) {
        buffer.addVertex(x1, y1, z1, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        buffer.addVertex(x2, y2, z2, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }
}
