package cf.leduyquang753.nbsrecorder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import cf.leduyquang753.nbsapi.Song;
import net.minecraft.client.gui.*;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class Save extends GuiScreen {
	private String fileName;
	private GuiTextField name, author, originalAuthor, description;
	private boolean goingToCancel = false;
	private boolean hiSpeed = false;
	private int sort = 2;
	
	private String getSortMode() {
		String s = "";
		switch (sort) {
		case 1: s = "Ascending"; break;
		case 2: s = "Descending"; break;
		default: s = "None";
		}
		return s;
	}
	
	private static String convertSongName(String name) {
		String result = name.trim();
		for (char c : ChatAllowedCharacters.allowedCharactersArray) {
			result = result.replace(c, '_');
		}
		return result + ".nbs";
	}
	
	@Override
	public void drawScreen(int X, int Y, float partialTicks) {
		this.drawDefaultBackground();
		this.fontRendererObj.drawStringWithShadow("Save song", (this.width-this.fontRendererObj.getStringWidth("Save song"))/2, 5, 16777215);
		this.fontRendererObj.drawStringWithShadow("Song name", 5, 25, 16777215);
		this.fontRendererObj.drawStringWithShadow((!name.getText().equalsIgnoreCase("") ? "Will be saved as " + fileName : "Please type in a name"), 5, 46,(!name.getText().equalsIgnoreCase("") ? 7763574 : 16711680));
		this.fontRendererObj.drawStringWithShadow("Song author", 5, 68, 16777215);
		this.fontRendererObj.drawStringWithShadow("Song original author", 5, 91, 16777215);
		this.fontRendererObj.drawStringWithShadow("Song description", 5, 114, 16777215);
		name.drawTextBox();
		author.drawTextBox();
		originalAuthor.drawTextBox();
		description.drawTextBox();
		super.drawScreen(X, Y, partialTicks);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		LocalDateTime now = LocalDateTime.now();
		String songInitName = now.getHour() + "h" + (now.getMinute() < 10 ? "0" : "") + now.getMinute() + "." + (now.getSecond() < 10 ? "0" : "") + now.getSecond() + " " + now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear();
		this.buttonList.add(new GuiButtonExt(0, 5, this.height-25, this.width/2-10, 20, "Save song"));
		this.buttonList.add(new GuiButtonExt(1, this.width/2+5, this.height-25, this.width/2-10, 20, "Cancel"));
		this.buttonList.add(new GuiButtonExt(100, 5, 176, this.width/2-10, 20, "Song speed: " + (hiSpeed ? "20 TPS" : "10 TPS")));
		this.buttonList.add(new GuiButtonExt(101, this.width/2+5, 176, this.width/2-10, 20, "Sort notes: " + getSortMode()));
		name = new GuiTextField(2, this.fontRendererObj, this.fontRendererObj.getStringWidth("Song name") + 10, 18, this.width-15-this.fontRendererObj.getStringWidth("Song name"), 20);
		author = new GuiTextField(3, this.fontRendererObj, this.fontRendererObj.getStringWidth("Song author") + 10, 61, this.width-15-this.fontRendererObj.getStringWidth("Song author"), 20);
		originalAuthor = new GuiTextField(4, this.fontRendererObj, this.fontRendererObj.getStringWidth("Song original author") + 10, 84, this.width-15-this.fontRendererObj.getStringWidth("Song original author"), 20);
		description = new GuiTextField(5, this.fontRendererObj, 5, 130, this.width-10, 20);
		name.setMaxStringLength(64);
		fileName = convertSongName("Song recorded at " + songInitName);
		name.setText("Song recorded at " + songInitName);
		author.setMaxStringLength(64);
		author.setText(mc.thePlayer.getName());
		originalAuthor.setMaxStringLength(64);
		songInitName = now.getHour() + "h" + (now.getMinute() < 10 ? "0" : "") + now.getMinute() + ":" + (now.getSecond() < 10 ? "0" : "") + now.getSecond() + " " + now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
		description.setMaxStringLength(65536);
		description.setText("Recorded with NBS Recorder at " + songInitName);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			Utils.div = hiSpeed ? 2 : 4;
			Utils.sort = sort != 0;
			if (sort != 0)
				Utils.comp = (n1, n2) -> new Integer(Utils.getPitch((sort == 1 ? n1 : n2).getPitch())).compareTo(new Integer(Utils.getPitch((sort == 2 ? n1 : n2).getPitch())));
			Song song = new Song(
					(short) 100,
					name.getText(),
					author.getText(),
					originalAuthor.getText(),
					description.getText(),
					(short) (hiSpeed ? 2000 : 1000),
					true,
					(byte) 30,
					(byte) 4,
					0,
					0,
					0,
					Main.notes,
					0,
					"",
					Utils.convert(Main.noteList));
			try {
				File file = new File(mc.mcDataDir.getPath() + "\\songs\\" + convertSongName(name.getText()));
				file.getParentFile().mkdirs();
				song.writeSong(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Main.saving = false;
				mc.setIngameFocus();
			}
			Main.saving = false;
			mc.setIngameFocus();
			break;
		case 1:
			if (!goingToCancel) {
				goingToCancel = true;
				button.enabled = false;
				button.displayString = "Press again to confirm";
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						button.enabled = true;
					}
				}, 1000);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						button.displayString = "Cancel";
						goingToCancel = false;
					}
				}, 5000);
			} else {
				mc.setIngameFocus();
				Main.saving = false;
			}
			break;
		case 100:
			hiSpeed = !hiSpeed;
			button.displayString = "Song speed: " + (hiSpeed ? "20 TPS" : "10 TPS");
			break;
		case 101:
			sort = (sort+1)%3;
			button.displayString = "Sort notes: " + getSortMode();
			break;
		}
	}
	
	@Override
	public void keyTyped(char typed, int code) {
		GuiButton saveButton = this.buttonList.get(0);
		if (name.isFocused()) {
			name.textboxKeyTyped(typed, code);
			if (name.getText().equalsIgnoreCase("")) {
				saveButton.enabled = false;
			} else {
				saveButton.enabled = true;
				fileName = convertSongName(name.getText());
			}
		}
		
		if (author.isFocused()) author.textboxKeyTyped(typed, code);
		if (originalAuthor.isFocused()) originalAuthor.textboxKeyTyped(typed, code);
		if (description.isFocused()) description.textboxKeyTyped(typed, code);
	}
	
	@Override
	public void mouseClicked(int X, int Y, int button) {
		name.mouseClicked(X, Y, button);
		author.mouseClicked(X, Y, button);
		originalAuthor.mouseClicked(X, Y, button);
		description.mouseClicked(X, Y, button);
		try {
			super.mouseClicked(X, Y, button);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
