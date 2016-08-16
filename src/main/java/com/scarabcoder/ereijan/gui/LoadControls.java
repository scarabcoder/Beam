package com.scarabcoder.ereijan.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.scarabcoder.ereijan.Main;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import scala.actors.threadpool.Arrays;

public class LoadControls extends GuiScreen{

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawBackground(0);
		this.buttonList.clear();
		if((new File("beam/controls/preset1.txt")).exists()){
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 40, 200, 20, "Controls 1"));
		}
		if((new File("beam/controls/preset2.txt")).exists()){
			this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 10, 200, 20, "Controls 2"));
		}
		if((new File("beam/controls/preset3.txt")).exists()){
			this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 2 + 20, 200, 20, "Controls 3"));
		}
		this.buttonList.add(new GuiButton(4, 0, 0, 80, 20, "Back"));
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	 protected void actionPerformed(GuiButton guibutton) {
		 File file = null;
		 if(guibutton.id < 4){
				file = new File("beam/controls/preset" + guibutton.id + ".txt");
				
			
				
		 
			if(file.exists()){
	         try {
				String data = FileUtils.readFileToString(file);
				
				for(String str : data.split("`")){
					List<String> list = new ArrayList<>(Arrays.asList(str.split("'")));
					Main.commands.put(Integer.valueOf(list.get(0)),list.get(1));
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		 }

		
	 }
}
