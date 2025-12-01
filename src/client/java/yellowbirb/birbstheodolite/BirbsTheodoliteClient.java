package yellowbirb.birbstheodolite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import yellowbirb.birbstheodolite.event.GameMessageHandler;
import yellowbirb.birbstheodolite.render.RenderManager;
import yellowbirb.birbstheodolite.render.shapes.Cube;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";

    @Override
    public void onInitializeClient() {

        RenderManager.add(new Cube(1, 0, 1, 0, 255, 255, 0, 255, false));

        WorldRenderEvents.LAST.register(RenderManager::draw);

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> GameMessageHandler.incoming(message));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RenderManager.clear());
    }
}