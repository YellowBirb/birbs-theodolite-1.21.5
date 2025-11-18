package yellowbirb.birbstheodolite.render.buffer;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

@UtilityClass
public class BufferManager {
    private int vao;
    private int vbo;

    private int prevVao;
    //private int prevVbo;

    public void init() {
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
        });
    }

    public void bindBuffer() {
        //prevVbo = glGetInteger(GL_ARRAY_BUFFER_BINDING);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
    }

    public void unbindBuffer() {
        glBindBuffer(GL_ARRAY_BUFFER, 0); // prevVbo instead of 0
    }

    public void writeBuffer(FloatBuffer buffer) {
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    public void draw(int drawMode, int vertices) {
        glDrawArrays(drawMode, 0, vertices);
    }

    public void bindArray() {
        prevVao = glGetInteger(GL_VERTEX_ARRAY_BINDING);
        glBindVertexArray(vao);
    }

    public void unbindArray() {
        glBindVertexArray(prevVao);
    }
}
