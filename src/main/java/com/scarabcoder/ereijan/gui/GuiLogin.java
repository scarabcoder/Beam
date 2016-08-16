package com.scarabcoder.ereijan.gui;

import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;

import com.scarabcoder.ereijan.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import pro.beam.api.BeamAPI;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.net.packet.Protocol.Report.TactileInfo;
import pro.beam.interactive.robot.RobotBuilder;

public class GuiLogin extends GuiScreen{
	int counter = 150;
	String error = "";
	public boolean loading = false;

	private boolean twof = false;

	private GuiTextField text;
	private GuiTextField pass;
	private GuiTextField twofa;


	private boolean remember = false;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.drawBackground(0);

		this.text.drawTextBox();
		if(this.counter != 100){
			this.counter += 1;
			this.drawCenteredString(fontRendererObj, error, this.width / 2, this.height / 2 + 70, 0xFF0000);
		}
		this.drawCenteredString(fontRendererObj, "- Beam Login -", this.width / 2, 10, 0xFFFFFF);
		this.pass.drawTextBox();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 + 80, 100, 20, "Login"));
		if(this.twof){
			this.twofa.drawTextBox();
		}
		String str2 = "No";

		if(this.twof){
			str2 = "Yes";
		}
		this.buttonList.add(new GuiButton(3, this.width / 2 + 10, this.height / 2 + 30, 90, 20, "2FA: " + str2));
		if(!this.twof){
			String str = "No";
			if(this.remember) str = "Yes";
			this.buttonList.add(new GuiButton(2, 5, 5, 125, 20, "Remember: " + str));
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	public void initGui()
	{
		this.text = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height/2 - 40, 200, 20);
		text.setMaxStringLength(23);
		text.setText("username");
		this.text.setFocused(false);

		this.pass = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height/2, 200, 20);
		pass.setMaxStringLength(23);
		pass.setText("password");
		this.pass.setFocused(false);

		if(this.twof){
			this.twofa = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, this.height/2 + 30, 100, 20);
			twofa.setMaxStringLength(23);
			twofa.setText("2FA Code");
			this.twofa.setFocused(true);
		}
	}
	protected void keyTyped(char par1, int par2)
	{
		try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.text.textboxKeyTyped(par1, par2);
		this.pass.textboxKeyTyped(par1, par2);
		if(twof){
			this.twofa.textboxKeyTyped(par1, par2);
		}
	}
	public void updateScreen()
	{
		super.updateScreen();
		this.text.updateCursorCounter();
		this.pass.updateCursorCounter();
		if(this.twof){
			this.twofa.updateCursorCounter();
		}
	}

	protected void mouseClicked(int x, int y, int btn) {
		try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.text.mouseClicked(x, y, btn);
		if(this.text.isFocused()){
			if(this.text.getText().equals("username")){
				this.text.setText("");
			}
		}
		this.pass.mouseClicked(x, y, btn);
		if(this.pass.isFocused()){
			if(this.pass.getText().equals("password")){
				this.pass.setText("");
			}
		}
		if(this.twof){
			this.twofa.mouseClicked(x, y, btn);
			if(this.twofa.isFocused()){
				if(this.twofa.getText().equals("2FA")){
					this.twofa.setText("");
				}
			}
		}
	}
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id){
		case 1:
			try {
				String str;
				try {
					str = Main.readUrl("https://beam.pro/api/v1/channels/" + this.text.getText() + "?fields=id");
					int streamID = Integer.parseInt(str.replace("{\"id\":", "").replace("}", ""));
					BeamAPI beam = new BeamAPI(); //An instance of the BeamAPI from beam-client-java

					Robot controller = new Robot();

					RobotBuilder builder = new RobotBuilder()
							.username(this.text.getText())
							.password(this.pass.getText())
							.channel(streamID);
					if(this.twof){
						builder.twoFactor(this.twofa.getText());
					}
					pro.beam.interactive.robot.Robot robot = builder.build(beam)
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
					if(this.remember){
						File file = new File("beam/login.txt");
						if(!file.exists()) file.createNewFile();
						FileUtils.writeStringToFile(file, this.text.getText() + ":" + this.pass.getText() + ":" + streamID);
					}
					mc.displayGuiScreen(new GuiMainMenu());
				} catch (InterruptedException e) {
					this.counter = 0;
					this.error = "Error, please make sure you have interactive mode on!";
					e.printStackTrace();
				} catch (ExecutionException e) {
					this.counter = 0;
					this.error = "Invalid login!";
					e.printStackTrace();
				}
			} catch (Exception e1) {
				this.counter = 0;
				this.error ="Channel ID for " + this.text.getText() + " not found!";
				e1.printStackTrace();
			}



			break;
		case 2:
			this.remember = !this.remember;
			break;
		case 3:
			this.twof = !this.twof;
			if(this.twof){
				this.twofa = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, this.height/2 + 30, 100, 20);
			}
			break;
		}
	}
}
