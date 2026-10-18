package com.example.client;

import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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
                if (client.player == null || client.interactionManager == null) {
                    return;
                }

                // 1. Kontrola Creative modu
                if (!client.player.isCreative()) {
                    client.player.sendMessage(Text.literal("§cMusis byt v Creative modu!"), false);
                    return;
                }

                // 2. Kontrola, zda je slot na boty prazdny
                if (!client.player.getEquippedStack(EquipmentSlot.FEET).isEmpty()) {
                    client.player.sendMessage(Text.literal("§cNejdriv si uvolni slot na boty!"), false);
                    return;
                }

                // 3. Vytvoreni truhly a vlozeni 64 diamantu dovnitr
                ItemStack chestStack = new ItemStack(Items.CHEST);
                ItemStack diamonds = new ItemStack(Items.DIAMOND, 64);

                chestStack.set(
                    DataComponentTypes.CONTAINER,
                    ContainerComponent.fromStacks(List.of(diamonds))
                );

                // 4. Pojmenovani truhly
                chestStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Truhla"));

                // 5. Vlozeni do slotu na boty + odeslani Creative paketu serveru (slot 8 = boty)
                client.player.equipStack(EquipmentSlot.FEET, chestStack);
                client.interactionManager.clickCreativeStack(chestStack, 8);

                client.player.sendMessage(Text.literal("§aTruhla byla vlozena do slotu na boty!"), false);
            }
        });
    }
}
