package yellowbirb.birbstheodolite;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.text.Text;
import yellowbirb.birbstheodolite.render.RenderManager;
import yellowbirb.birbstheodolite.render.shapes.CircleXZ;
import yellowbirb.birbstheodolite.render.shapes.Line;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";

    @Override
    public void onInitializeClient() {

        RenderManager.add(new Line(0, 0, 0, 0, 1, 0, 255, 0, 0, 255, false));
        RenderManager.add(new CircleXZ(3f, 0.5f, 0f, 3f, 0f, 255, 255, 0, 255, true));

        WorldRenderEvents.LAST.register(RenderManager::draw);

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            // [15:25:29] [Render thread/INFO]: [STDOUT]: false ; empty[style={!italic}, siblings=[literal{The target is around }[style={color=green}], literal{35 blocks below}[style={color=yellow}], literal{, at a }[style={color=green}], literal{10 degrees }[style={color=aqua}], literal{angle!}[style={color=green}]]]
            // [15:25:29] [Render thread/INFO]: [CHAT] The target is around 35 blocks below, at a 10 degrees angle!

            // [15:25:40] [Render thread/INFO]: [STDOUT]: false ; empty[style={!italic}, siblings=[literal{The target is around }[style={color=green}], literal{80 blocks below}[style={color=yellow}], literal{, at a }[style={color=green}], literal{55 degrees }[style={color=aqua}], literal{angle!}[style={color=green}]]]
            // [15:25:40] [Render thread/INFO]: [CHAT] The target is around 80 blocks below, at a 55 degrees angle!

            // [15:25:44] [Render thread/INFO]: [STDOUT]: false ; empty[style={!italic}, siblings=[literal{Killing the animal rewarded you }[style={color=green}], literal{4 pelts}[style={color=dark_purple}], literal{.}[style={color=green}]]]
            // [15:25:44] [Render thread/INFO]: [CHAT] Killing the animal rewarded you 4 pelts.
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("drawdiamond")
                    .then(ClientCommandManager.argument("x", FloatArgumentType.floatArg())
                            .then(ClientCommandManager.argument("y", FloatArgumentType.floatArg())
                                    .then(ClientCommandManager.argument("z", FloatArgumentType.floatArg())
                                            .executes(this::executeTestCommand)))));
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("stopdraw").executes(this::executeStopDraw));
        });
    }

    private int executeTestCommand(CommandContext<FabricClientCommandSource> context) {
        float x = FloatArgumentType.getFloat(context, "x");
        float y = FloatArgumentType.getFloat(context, "y");
        float z = FloatArgumentType.getFloat(context, "z");
        context.getSource().sendFeedback(Text.literal("Called /drawdiamond " + x + " " + y + " " + z));
        RenderManager.add(new CircleXZ(3f, 0.5f, x, y, z, 255, 255, 0, 255, true));
        return 1;
    }

    private int executeStopDraw(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("Called /stopdraw"));
        RenderManager.clear();
        return 1;
    }
}