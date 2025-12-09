package yellowbirb.birbstheodolite;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import yellowbirb.birbstheodolite.event.GameMessageHandler;
import yellowbirb.birbstheodolite.render.RenderManager;

public class BirbsTheodoliteClient implements ClientModInitializer {

    public static final String MOD_ID = "birbs-theodolite";
    private static final String MODRINTH_PROJECT_API_LINK = "https://api.modrinth.com/v2/project/birbs-theodolite/version";

    @Override
    public void onInitializeClient() {

        // draw stuff
        WorldRenderEvents.LAST.register(RenderManager::draw);

        // Listen to Messages sent by the server
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> GameMessageHandler.incoming(message));

        // check for updates when joining a server
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> checkForUpdate());

        // stop drawing stuff when leaving the lobby
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> RenderManager.clear());
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> RenderManager.clear());

        // manually stop drawing stuff
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("clearTheodolite").executes((context) -> {
                    RenderManager.clear();
                    return 1;
                }
        )));
    }

    private void checkForUpdate() {
        // TODO: implement
    }
}