package com.scarabcoder.ereijan;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import com.scarabcoder.ereijan.gui.ControlPanel;
import com.scarabcoder.ereijan.gui.GuiLogin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pro.beam.api.BeamAPI;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.Report.TactileInfo;
import pro.beam.interactive.robot.RobotBuilder;
import scala.actors.threadpool.Arrays;

public class Events {
	
	private int counter = 0;
	
	public Minecraft mc = Main.mc;
	
	@SubscribeEvent
	public void keyPress(KeyInputEvent e){
		if(Keybinds.openGui.isKeyDown()){
			mc.displayGuiScreen(new ControlPanel());
		}
	}
	   @SubscribeEvent(priority = EventPriority.NORMAL)
	   public void eventHandler(RenderGameOverlayEvent event){
		   mc.fontRendererObj.drawString("Beam Interactive by ScarabCoder", 5, mc.displayHeight - 15, 0xFFFFFF);
	   }
	
	@SubscribeEvent
	public void tickEvent(TickEvent e){
		if(mc.currentScreen instanceof GuiMainMenu){
			if(!Main.isConnected){
				File file = new File("beam/login.txt");
				if(file.exists()){
					try {
					List<String> auth;
					auth = new ArrayList<>(Arrays.asList(FileUtils.readFileToString(file).split(":")));
					
					BeamAPI beam = new BeamAPI(); //An instance of the BeamAPI from beam-client-java

						Robot controller = new Robot();
					
						pro.beam.interactive.robot.Robot robot = new RobotBuilder()
						        .username(auth.get(0))
						        .password(auth.get(1))
						        .channel(Integer.valueOf(auth.get(2)))
						        .build(beam)
						        .get();
						robot.on(Protocol.Report.class, report -> {
						    // If we have any joysticks in the report
							Main.report = report;
						    
						    if(Main.isHolding.isEmpty()){
						    	for(TactileInfo info : report.getTactileList()){
									if(info.getHolding() > 0){
										Main.isHolding.put(info.getId(), true);
									}else{
										Main.isHolding.put(info.getId(), false);
									}
								}
						    }
						});
						
						Main.isConnected = true;
					} catch (NumberFormatException | InterruptedException | IOException | AWTException | ExecutionException e1) {
						mc.displayGuiScreen(new GuiLogin());
						file.delete();
						e1.printStackTrace();
					}
				}else{
					mc.displayGuiScreen(new GuiLogin());
				}
			}
		}
		
		if(mc.thePlayer != null){
			//if(counter == 55){
				for(TactileInfo info : Main.report.getTactileList()){
					if(info.getHolding() > 0){
						if(!Main.isHolding.get(info.getId())){
								if(Main.commands.containsKey(info.getId())){
									mc.thePlayer.sendChatMessage(Main.commands.get(info.getId()));
								}
						}
						Main.isHolding.put(info.getId(), true);
					}else{
						Main.isHolding.put(info.getId(), false);
					}
				}
				counter = 0;
			//}else{
				//counter += 1;
			//}
		}
	}
	
}
