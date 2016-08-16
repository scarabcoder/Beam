package com.scarabcoder.ereijan.gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

import com.scarabcoder.ereijan.Main;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class SaveControls extends GuiScreen{

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawBackground(0);
		this.buttonList.clear();
		if((new File("beam/controls/preset1.txt")).exists()){
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 40, 200, 20, "Controls 1"));
		}else{
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 40, 200, 20, "< Empty >"));
		}
		if((new File("beam/controls/preset2.txt")).exists()){
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 10, 200, 20, "Controls 2"));
		}else{
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 10, 200, 20, "< Empty >"));
		}
		if((new File("beam/controls/preset3.txt")).exists()){
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + 20, 200, 20, "Controls 3"));
		}else{
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + 20, 200, 20, "< Empty >"));
		}
		this.buttonList.add(new GuiButton(4, 0, 0, 80, 20, "Back"));
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	 protected void actionPerformed(GuiButton guibutton) {
		 File file = null;
		 if(guibutton.id < 4){
				file = new File("beam/controls/preset" + guibutton.id + ".txt");
				
			
		 
			if(file.exists()){
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         try {
	        	 String str = "";
				for(int key : Main.commands.keySet()){
					str = str + key + "'" + Main.commands.get(key) + "`";
				}
				FileUtils.writeStringToFile(file, str);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		 }
		 mc.displayGuiScreen(new ControlPanel());
	 }
}
