package com.scarabcoder.ereijan.gui;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.scarabcoder.ereijan.Main;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import pro.beam.interactive.net.packet.Protocol.Report.TactileInfo;

public class ControlPanel extends GuiScreen {
	
	private List<GuiTextField> textFields = new ArrayList<GuiTextField>();
	
	private List<TactileInfo> buttons;
	
	private int type = 1;
	
	public void initGui()
    {	
		
		this.buttons = Main.report.getTactileList();
		if(this.type == 1){
		int x = 1;
		for(TactileInfo info : this.buttons){ 
			GuiTextField field = new GuiTextField(x, this.fontRendererObj, 150, 46 + 25 * (x - 1), 150, 20);
			field.setMaxStringLength(500);
			field.setText("");
			if(Main.commands.containsKey(info.getId())){
				field.setText(Main.commands.get(info.getId()));
			}
			field.setFocused(false);
			textFields.add(field);
			x += 1;
		}
		}
        
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawBackground(0);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, 30, 20, 125, 20, "Save Controls"));
		this.buttonList.add(new GuiButton(2, this.width - (125 + 30), 20, 125, 20, "Load Controls"));
		if(type == 1){
			int x = 0;
			for(TactileInfo button : this.buttons){
				this.drawString(fontRendererObj, "Button ID " + button.getId(), 20, 50 + 25 * x, 0xFFFFFF);
				x += 1;
			}
		}
        for(GuiTextField field : this.textFields){
        	field.drawTextBox();
        }
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	protected void keyTyped(char par1, int par2)
    {
        try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int x = 0;
        for(GuiTextField field : this.textFields){
        	field.textboxKeyTyped(par1, par2);
        	if(field.isFocused()){
        		Main.commands.put(this.buttons.get(x).getId(), field.getText());
        	}
        	x += 1;
        }
    }
    public void updateScreen()
    {
        super.updateScreen();
        for(GuiTextField field : this.textFields){
        	field.updateCursorCounter();
        }
    }
    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(GuiTextField field : this.textFields){
        	field.mouseClicked(x, y, btn);
        }
    }
    protected void actionPerformed(GuiButton guibutton) {
    	switch(guibutton.id){
    	case 1:
    		mc.displayGuiScreen(new SaveControls());
    		break;
    	case 2:
    		mc.displayGuiScreen(new LoadControls());
    		break;
    	}
    }
}
