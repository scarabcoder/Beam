package com.scarabcoder.ereijan;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {
	public static KeyBinding openGui;

    public static void init() {
        // Define the "ping" binding, with (unlocalized) name "key.ping" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 24 ("O", LWJGL constant: Keyboard.KEY_O)
        openGui = new KeyBinding("key.openGui", Keyboard.KEY_B, "key.categories.beam");
        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(openGui);
}
}
