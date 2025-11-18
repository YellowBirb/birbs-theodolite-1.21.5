package yellowbirb.birbstheodolite.render.shader;

import lombok.Getter;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Shader {
    @Getter
    private final int id;
    private int prevShader;


    public Shader(String name) {
        int v = ShaderManager.loadShaderProgram(name, ShaderManager.ShaderType.VERTEX);
        int f = ShaderManager.loadShaderProgram(name, ShaderManager.ShaderType.FRAGMENT);
        this.id = glCreateProgram();
        System.out.println("Shader ID: " + this.id);
        glAttachShader(id, v);
        glAttachShader(id, f);
        glLinkProgram(id);
    }

    public void bind() {
        prevShader = glGetInteger(GL_CURRENT_PROGRAM);
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
        // glUseProgram(prevShader);
    }

    public void uniformMatrix4f(String name, FloatBuffer matrix) {
        bind();
        glUniformMatrix4fv(glGetUniformLocation(id, name), false, matrix);
        unbind();
    }

    public void uniformValue2f(String name, float value1, float value2) {
        bind();
        glUniform2f(glGetUniformLocation(id, name), value1, value2);
        unbind();
    }
}
