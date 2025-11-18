package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import yellowbirb.birbstheodolite.render.buffer.BufferManager;
import yellowbirb.birbstheodolite.render.shader.ShaderManager;

@UtilityClass
public class RenderSystem {
    public void init() {
        BufferManager.init();
        ShaderManager.init();
    }
}
