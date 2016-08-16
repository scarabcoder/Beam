package com.scarabcoder.ereijan.ScarabUtil;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ScarabUtil {
	public static void chat(String msg){
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentString("[" + ChatFormatting.BLUE + "Beam" + ChatFormatting.RESET + "] " + msg));
	}
	
	
	
}
