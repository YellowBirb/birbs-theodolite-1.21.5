package yellowbirb.birbstheodolite.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.OptionalDouble;
import java.util.OptionalInt;

@UtilityClass
public class Renderer {

    private static final BufferAllocator allocator = new BufferAllocator(RenderLayer.CUTOUT_BUFFER_SIZE);

    public void drawSingleLine(WorldRenderContext ctx, RenderPipeline pipeline,
                               float x0, float y0, float z0, int r0, int g0, int b0, int a0,
                               float x1, float y1, float z1, int r1, int g1, int b1, int a1) {
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();

        assert matrices != null;
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        BufferBuilder bufferBuilder = getBufferBuilder(pipeline);

        VertexHelper.singleLine(matrices, bufferBuilder,
                x0, y0, z0, r0, g0, b0, a0,
                x1, y1, z1, r1, g1, b1, a1);

        draw(pipeline, bufferBuilder.end());

        matrices.pop();
    }

    public void drawCircleXZ(WorldRenderContext ctx, RenderPipeline pipeline,
                             float radius, float segmentLength, float x, float y, float z,
                             int r, int g, int b, int a) {
        MatrixStack matrices = ctx.matrixStack();
        Vec3d cam = ctx.camera().getPos();

        assert matrices != null;
        matrices.push();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        BufferBuilder bufferBuilder = getBufferBuilder(pipeline);

        VertexHelper.circleXZ_Line_Strip(matrices, bufferBuilder,
                radius, segmentLength, x, y, z,
                r, g, b, a);

        draw(pipeline, bufferBuilder.end());

        matrices.pop();
    }

    private BufferBuilder getBufferBuilder(RenderPipeline pipeline) {
        return new BufferBuilder(allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());
    }

    private void draw(RenderPipeline pipeline, BuiltBuffer buffer) {
        GpuBuffer vertices = pipeline.getVertexFormat().uploadImmediateVertexBuffer(buffer.getBuffer());

        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (buffer.getSortedBuffer() == null) {
            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(buffer.getDrawParameters().mode());
            indices = shapeIndexBuffer.getIndexBuffer(buffer.getDrawParameters().indexCount());
            indexType = shapeIndexBuffer.getIndexType();
        } else {
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(buffer.getSortedBuffer());
            indexType = buffer.getDrawParameters().indexType();
        }

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(MinecraftClient.getInstance().getFramebuffer().getColorAttachment(), OptionalInt.empty(), MinecraftClient.getInstance().getFramebuffer().getDepthAttachment(), OptionalDouble.empty())) {

            renderPass.setPipeline(pipeline);

            renderPass.setUniform("Projection", RenderSystem.getProjectionMatrix());

            if (RenderSystem.SCISSOR_STATE.isEnabled()) {
                renderPass.enableScissor(RenderSystem.SCISSOR_STATE);
            }


            renderPass.setVertexBuffer(0, vertices);

            renderPass.setIndexBuffer(indices, indexType);


            renderPass.drawIndexed(0, buffer.getDrawParameters().indexCount());
        }

        buffer.close();
    }

    public void close() {
        allocator.close();
    }
}
