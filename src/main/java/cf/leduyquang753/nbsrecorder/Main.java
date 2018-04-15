package cf.leduyquang753.nbsrecorder;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "nbsrecorder", name = "NBS Recorder", version = "0.3")
public class Main {
	public static boolean recording = false;
	public static boolean saving = false;
	public static int ticks;
	public static int layers;
	public static int notes;
	public static List<RecordedNote> noteList = new ArrayList<RecordedNote>();
	
	@EventHandler
	public void onInit(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Events());
		Keys.register();
	}
}
