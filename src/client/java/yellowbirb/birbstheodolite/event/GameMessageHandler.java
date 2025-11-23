package yellowbirb.birbstheodolite.event;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import yellowbirb.birbstheodolite.render.RenderManager;
import yellowbirb.birbstheodolite.render.shapes.CircleXZ;

import java.util.Optional;

@UtilityClass
public class GameMessageHandler {

    private final String THEODOLITE_MESSAGE = "The target is around [0-9]+ blocks (below|above), at a [1-9][0-9]? degrees angle!";
    private final String PELT_REWARD_MESSAGE = "Killing the animal rewarded you [1-9][0-9]? pelts.";
    private final String SERVER_CHANGE_MESSAGE = "Sending to server .+\\.\\.\\.";

    private final String[] RELEVANT_MESSAGES = {THEODOLITE_MESSAGE, PELT_REWARD_MESSAGE, SERVER_CHANGE_MESSAGE};


    private int isRelevant(String msg) {

        if (msg == null || msg.isEmpty() || msg.startsWith(" ")) return -1;

        for (int i = 0; i < RELEVANT_MESSAGES.length; i++) {
            if (msg.charAt(0) == RELEVANT_MESSAGES[i].charAt(0)) {
                if (msg.matches(RELEVANT_MESSAGES[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void incoming(Text text) {

        final String[] messageArr = {""};

        text.visit((string) -> {
            messageArr[0] = messageArr[0] + string;
            return Optional.empty();
        });

        String message = messageArr[0];

        int messageType = isRelevant(message);

        if (messageType == -1) return;

        switch (messageType) {
            case 0:
                onReceiveTheodoliteMessage(message);
                break;
            case 1:
                onReceivePeltRewardMessage();
                break;
            case 2:
                onReceiveServerChangeMessage();
                break;
        }
    }

    private void onReceiveTheodoliteMessage(String message) {
        String[] words = message.split("\\s");

        int deltaY = Integer.parseInt(words[4]);
        int alpha = 90 - Integer.parseInt(words[9]);

        if (alpha == 90) {
            MinecraftClient.getInstance().player.sendMessage(MutableText.of(PlainTextContent.of("§3[Birb's Theodolite] §cCannot calculate with 0 degree angle")), false);
            return;
        }

        if (words[6].equals("below,")) {
            deltaY = -deltaY;
        }

        double factor = (2 * Math.PI) / 360;
        double alpha_rad1 = (alpha + 1.5) * factor;
        double alpha_rad2 = alpha * factor;
        double alpha_rad3 = (alpha - 1.5) * factor;

        float radius1 = (float) (Math.abs(Math.tan(alpha_rad1) * deltaY));
        float radius2 = (float) (Math.abs(Math.tan(alpha_rad2) * deltaY));
        float radius3 = (float) (Math.abs(Math.tan(alpha_rad3) * deltaY));

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        float playerX = (float) player.getX();
        float playerY = (float) player.getY();
        float playerZ = (float) player.getZ();

        RenderManager.add(new CircleXZ(radius1, 0.5f, playerX, playerY + deltaY, playerZ, 0, 255, 0, 255, true));
        RenderManager.add(new CircleXZ(radius2, 0.5f, playerX, playerY + deltaY, playerZ, 255, 0, 0, 255, true));
        RenderManager.add(new CircleXZ(radius3, 0.5f, playerX, playerY + deltaY, playerZ, 0, 255, 0, 255, true));
    }

    private void onReceivePeltRewardMessage() {
        RenderManager.clear();
    }

    private void onReceiveServerChangeMessage() {
        RenderManager.clear();
    }

}
