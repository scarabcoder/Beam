package com.scarabcoder.ereijan;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.scarabcoder.ereijan.ScarabUtil.Strings;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import pro.beam.interactive.net.packet.Protocol.Report;

@Mod(modid = Strings.id, name = Strings.name, version = Strings.version)
public class Main {
	
	public static HashMap<Integer, String> commands = new HashMap<Integer, String>();
	public static Minecraft mc = Minecraft.getMinecraft();
	
	
	
	public static Report report;
	
	public static HashMap<Integer, Boolean> isHolding = new HashMap<Integer, Boolean>();
	
	public static boolean isConnected = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) throws AWTException{
		MinecraftForge.EVENT_BUS.register(new Events());
		File folder = new File("beam/controls");
		if(!folder.exists()){
			System.out.println(folder.mkdirs());
			
		}
		Keybinds.init();
	}
	
	public static String readUrl(String urlString) throws Exception {
		
		
	    BufferedReader reader = null;
	    
	    
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	        	
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	    
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		
	}
	

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
	}
}
