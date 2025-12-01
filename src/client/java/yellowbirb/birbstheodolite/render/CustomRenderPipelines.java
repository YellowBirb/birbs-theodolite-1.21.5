package yellowbirb.birbstheodolite.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import yellowbirb.birbstheodolite.BirbsTheodoliteClient;

@UtilityClass
public class CustomRenderPipelines {

    private static final RenderPipeline.Snippet RENDERTYPE_LINES_SNIPPET_NO_FOG = RenderPipeline.builder(new RenderPipeline.Snippet[]{RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET})
            .withVertexShader("core/rendertype_lines")
            .withFragmentShader("core/rendertype_lines")
            .withBlend(BlendFunction.TRANSLUCENT)
            .withCull(false)
            .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINES)
            .buildSnippet();

    // -----------------------------------------------------------------------------------------------------------------

    public static final RenderPipeline LINES = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/lines"))
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline LINES_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET_NO_FOG)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/lines_through_walls"))
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline LINE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/line_strip"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINE_STRIP)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline LINE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET_NO_FOG)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/line_strip_through_walls"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR_NORMAL, VertexFormat.DrawMode.LINE_STRIP)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/triangle_strip"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline TRIANGLE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.TRIANGLE_STRIP)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline QUADS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline QUADS_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of(BirbsTheodoliteClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );
}
