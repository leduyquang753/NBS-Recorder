package cf.leduyquang753.nbsrecorder;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keys {
	public static KeyBinding startStop;
	
	public static void register() {
		startStop = new KeyBinding("Start/Stop recording", Keyboard.KEY_HOME, "NBS Recorder");
		ClientRegistry.registerKeyBinding(startStop);
	}
}
