package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    private static KeyBinding giveChestKey;

    @Override
    public void onInitializeClient() {
        giveChestKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Troll Chest",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_SEMICOLON,
            KeyBinding.Category.CREATIVE
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (giveChestKey.wasPressed()) {
                if (client.player != null) {
                    String myCommand = "give @s chest[container=[{item:{id:\"minecraft:diamond\",count:64},slot:0}]] 1";
                    client.player.networkHandler.sendChatCommand(myCommand);
                }
            }
        });
    }
}
