package yellowbirb.birbstheodolite.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.joml.Vector4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import yellowbirb.birbstheodolite.BirbsTheodoliteClient;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class RenderUtils_ABANDONED {

    public void extractAndDrawWaypoint(WorldRenderContext context) {
        renderWaypoint(context);
        drawWireframeThroughWalls(MinecraftClient.getInstance(), WIREFRAME_THROUGH_WALLS);
    }

    private static final RenderPipeline WIREFRAME_THROUGH_WALLS = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
            .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/wireframe_through_walls"))
            /*.withPolygonMode(PolygonMode.WIREFRAME)*/
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build()
    );

    private static final BufferAllocator allocator = new BufferAllocator(RenderLayer.CUTOUT_BUFFER_SIZE);
    private BufferBuilder buffer;

    private void renderWaypoint(WorldRenderContext ctx) {
        MatrixStack matrices = ctx.matrixStack();
        Vec3d camera = ctx.camera().getPos();

        assert matrices != null;
        matrices.push();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        if (buffer == null) {
            buffer = new BufferBuilder(allocator, WIREFRAME_THROUGH_WALLS.getVertexFormatMode(), WIREFRAME_THROUGH_WALLS.getVertexFormat());
        }

        VertexRendering.drawFilledBox(matrices, buffer, 0f, 100f, 0f, 1f, 101f, 1f, 0f, 1f, 0f, 0.5f);

        matrices.pop();
    }

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);

    private void drawWireframeThroughWalls(MinecraftClient client, RenderPipeline pipeline) {

        BuiltBuffer builtBuffer = buffer.end();
        BuiltBuffer.DrawParameters drawParameters = builtBuffer.getDrawParameters();
        VertexFormat format = drawParameters.format();

        //GpuBuffer vertices = upload(drawParameters, format, builtBuffer);

        // ====================================================draw=====================================================

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(client.getFramebuffer().getColorAttachment(), OptionalInt.empty(), client.getFramebuffer().getDepthAttachment(), OptionalDouble.empty())) {
            renderPass.setPipeline(pipeline);

            renderPass.draw(1, 4);
        }

        // =============================================================================================================

        //vertexBuffer.rotate();
        buffer = null;
    }


    public void close() {
        allocator.close();
    }

}
