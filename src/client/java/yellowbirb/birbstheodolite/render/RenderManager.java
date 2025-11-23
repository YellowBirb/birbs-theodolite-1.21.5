package yellowbirb.birbstheodolite.render;

import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import yellowbirb.birbstheodolite.render.shapes.RenderShape;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RenderManager {

    private final List<RenderShape> shapeList = new ArrayList<>();

    public void add(RenderShape shape) {
        shapeList.add(shape);
    }

    public RenderShape remove(int index) {
        return shapeList.remove(index);
    }

    public RenderShape removeFirst() {
        return shapeList.removeFirst();
    }

    public RenderShape removeLast() {
        return shapeList.removeLast();
    }

    public void clear() {
        shapeList.clear();
    }

    public void draw(WorldRenderContext ctx) {
        for (RenderShape shape : shapeList) {
            Renderer.drawShape(ctx, shape);
        }
    }

}
