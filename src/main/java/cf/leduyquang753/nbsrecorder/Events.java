package cf.leduyquang753.nbsrecorder;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Events {
	Minecraft mc = Minecraft.getMinecraft();
	
	private static String getTimeString(int dur) {
		int duration = dur-1;
		int secs = duration/20;
		int ticks = duration%20/2;
		int hours = secs/3600;
		int minutes = (secs%3600)/60;
		String minString = (minutes < 10 && hours > 0) ? "0" + minutes : minutes + "";
		int seconds = secs%60;
		String secString = (seconds < 10 && secs > 59) ? "0" + seconds : seconds + "";
		return (hours > 0 ? hours + "h" : "") + ((hours == 0 && minutes > 0) ? minString + ":" : "") + secString + "," + ticks;
	}
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {
		if (event.type == ElementType.EXPERIENCE && !event.isCancelable()) {
			boolean enable = GL11.glIsEnabled(3042);
			GL11.glDisable(3042);
			
			mc.fontRendererObj.drawStringWithShadow(Main.saving ? "Saving..." : (Main.recording ? "Recording..." : "") , 5, 5, 16777215);
			if (Main.recording) {
				mc.fontRendererObj.drawStringWithShadow(getTimeString(Main.ticks/2), 5, 15, 16777215);
				mc.fontRendererObj.drawStringWithShadow(Main.notes + "", 5, 25, 16777215);
			}
			
			if (enable) GL11.glEnable(3042);
		}
	}
	
	@SubscribeEvent
	public void onSound(PlaySoundEvent event) {
		if (!Utils.isNote(event.name)) return;
		Main.noteList.add(new RecordedNote(Main.ticks, event.name, event.sound.getPitch()));
		Main.notes++;
	}
	
	@SubscribeEvent
	public void onKeyPress(KeyInputEvent event) {
		if (Keys.startStop.isPressed()) {
			if (!Main.recording) {
				if (Main.saving) return;
				Main.noteList.clear();
				Main.ticks = 0;
				Main.notes = 0;
				Main.recording = true;
			} else {
				if (Main.notes == 0) {
					Main.recording = false;
					return;
				}
				Main.recording = false;
				Main.saving = true;
				mc.displayGuiScreen(new Save());
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if (Main.recording && Main.notes > 0) {
			Main.ticks++;
		}
	}
}
